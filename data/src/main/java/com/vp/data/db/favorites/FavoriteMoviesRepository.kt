package com.vp.data.db.favorites

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vp.data.db.favorites.dao.FavoriteMoviesDao
import com.vp.data.db.favorites.entity.FavoriteMovieEntity

class FavoriteMoviesRepository(private val favoriteMoviesDao: FavoriteMoviesDao) {

    val allFavorites: LiveData<List<FavoriteMovieEntity>> = favoriteMoviesDao.getAllFavorites()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(favoriteMovieEntity: FavoriteMovieEntity) {
        favoriteMoviesDao.insert(favoriteMovieEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(favoriteMovieEntity: FavoriteMovieEntity) {
        favoriteMoviesDao.delete(favoriteMovieEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getFavoriteById(movieId: String): LiveData<FavoriteMovieEntity>? {
        return favoriteMoviesDao.getFavoriteById(movieId)
    }
}