package com.vp.detail.viewmodel

import android.app.Application
import android.util.Log
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

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

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

    fun addToFavoriteMovies(movieId: String) = scope.launch(Dispatchers.IO) {
        val favoriteMovie = FavoriteMovieEntity(movieId, details.value?.poster)
        Log.e("addToFavoriteMovies", favoriteMovie.toString())
        repository.insert(favoriteMovie)
    }

    fun deleteFromFavoriteMovies(movieId: String) = scope.launch(Dispatchers.IO) {
        //ToDo: Check for bugs in case of null poster
        val favoriteMovie = FavoriteMovieEntity(movieId, details.value?.poster)
        repository.delete(favoriteMovie)
    }

    fun isFavoriteMovie(movieId: String): LiveData<FavoriteMovieEntity>? {
        return repository.getFavoriteById(movieId)
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}