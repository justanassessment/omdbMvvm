package com.vp.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vp.data.db.favorites.FavoriteMoviesDatabase
import com.vp.data.db.favorites.FavoriteMoviesRepository
import com.vp.data.db.favorites.entity.FavoriteMovieEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteMoviesViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val mRepository: FavoriteMoviesRepository

    private val allFavoriteMovies: LiveData<List<FavoriteMovieEntity>>

    init {
        val favoriteDao = FavoriteMoviesDatabase.getDatabase(application).favoriteDao()
        mRepository = FavoriteMoviesRepository(favoriteDao)
        allFavoriteMovies = mRepository.allFavorites
    }

    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovieEntity>> {
        return allFavoriteMovies
    }

    fun insertFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) = viewModelScope.launch {
        mRepository.insert(favoriteMovieEntity)
    }

    fun deleteFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) = viewModelScope.launch {
        mRepository.delete(favoriteMovieEntity)
    }
}