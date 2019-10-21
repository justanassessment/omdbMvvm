package com.vp.data.db.favorites

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vp.data.db.favorites.dao.FavoriteMoviesDao
import com.vp.data.db.favorites.entity.FavoriteMovieEntity

@Database(entities = arrayOf(FavoriteMovieEntity::class), version = 1, exportSchema = false)
abstract class FavoriteMoviesDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteMoviesDao

    companion object {
        @Volatile
        private var sINSTANCE: FavoriteMoviesDatabase? = null

        fun getDatabase(context: Context): FavoriteMoviesDatabase {
            val tempInstance = sINSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteMoviesDatabase::class.java,
                    "favorites_db"
                ).build()
                sINSTANCE = instance
                return instance
            }
        }
    }

}
