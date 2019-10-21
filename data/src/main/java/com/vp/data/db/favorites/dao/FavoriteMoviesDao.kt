package com.vp.data.db.favorites.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vp.data.db.favorites.entity.FavoriteMovieEntity

@Dao
interface FavoriteMoviesDao {

    @Query("SELECT * from favorites_table")
    fun getAllFavorites(): LiveData<List<FavoriteMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteMovie: FavoriteMovieEntity)

    @Delete
    fun delete(favoriteMovie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorites_table WHERE imdbID = :query LIMIT 1")
    fun getFavoriteById(query: String): LiveData<FavoriteMovieEntity>?

    @Query("DELETE FROM favorites_table")
    fun deleteAll()
}