package com.vp.detail.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vp.data.db.favorites.FavoriteMoviesDatabase
import com.vp.data.db.favorites.FavoriteMoviesRepository
import com.vp.data.db.favorites.entity.FavoriteMovieEntity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback
import kotlin.coroutines.CoroutineContext

class DetailsViewModel @Inject constructor(private val detailService: DetailService, application: Application) : AndroidViewModel(application) {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val repository: FavoriteMoviesRepository

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val favoriteMoviesDao = FavoriteMoviesDatabase.getDatabase(getApplication()).favoriteDao()
        repository = FavoriteMoviesRepository(favoriteMoviesDao)
    }

    /**
     * @return title (LiveData) of a specified movie
     */
    fun title(): LiveData<String> = title

    /**
     * @return details (LiveData) of a specified movie
     */
    fun details(): LiveData<MovieDetail> = details

    /**
     * @return current loading state (LiveData) of a clicked movie. It can be: 1) loaded, 2) in progress or 3) error
     */
    fun state(): LiveData<LoadingState> = loadingState

    /**
     * Fetches details of a specified movie
     *
     * @param movieId imdbID of a clicked movie
     */
    fun fetchDetails(movieId: String) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())
                response?.body()?.title?.let {
                    title.postValue(it)
                }
                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    /**
     * Adds a specified movie to a list (local RoomDataBase) of favorite movies
     *
     * @param movieId imdbID of a specified movie
     */
    fun addToFavoriteMovies(movieId: String) = scope.launch(Dispatchers.IO) {
        val favoriteMovie = FavoriteMovieEntity(movieId, details.value?.poster)
        repository.insert(favoriteMovie)
    }

    /**
     * Deletes a specified movie to a list (local RoomDataBase) of favorite movies
     *
     * @param movieId imdbID of a specified movie
     */
    fun deleteFromFavoriteMovies(movieId: String) = scope.launch(Dispatchers.IO) {
        //ToDo: Check for bugs in case of null poster
        val favoriteMovie = FavoriteMovieEntity(movieId, details.value?.poster)
        repository.delete(favoriteMovie)
    }

    /**
     * Returns favorite movie entity (LiveData) of a specified movie
     *
     * @param movieId imdbID of a specified movie
     * @return {@code LiveData<FavoriteMovieEntity>}
     */
    fun getFavoriteMovieById(movieId: String): LiveData<FavoriteMovieEntity>? {
        return repository.getFavoriteById(movieId)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}