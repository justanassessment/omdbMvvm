package com.vp.data.db.favorites

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vp.data.db.favorites.dao.FavoriteMoviesDao
import com.vp.data.db.favorites.entity.FavoriteMovieEntity

class FavoriteMoviesRepository(private val favoriteMoviesDao: FavoriteMoviesDao) {

    val allFavorites: LiveData<List<FavoriteMovieEntity>> = favoriteMoviesDao.getAllFavorites()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(favoriteMovieEntity: FavoriteMovieEntity) {
        Log.e("insert", favoriteMovieEntity.toString())
        favoriteMoviesDao.insert(favoriteMovieEntity)
        Log.e("insert", favoriteMoviesDao.getAllFavorites().toString())
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