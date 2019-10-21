package com.vp.data.db.favorites.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class FavoriteMovieEntity (
    @PrimaryKey
    var imdbID: String,
    var poster: String?
)