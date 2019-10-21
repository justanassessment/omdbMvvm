package com.vp.favorites.di

import com.vp.favorites.FavoriteMoviesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteMoviesFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindFavoriteMoviesFragment(): FavoriteMoviesFragment
}
