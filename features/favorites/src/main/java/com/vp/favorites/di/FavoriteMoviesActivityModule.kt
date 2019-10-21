package com.vp.favorites.di

import com.vp.favorites.FavoriteMoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FavoriteMoviesActivityModule {

    @ContributesAndroidInjector(modules = [FavoriteMoviesFragmentModule::class, FavoriteMoviesViewModelModule::class])
    abstract fun bindFavoriteMoviesActivity(): FavoriteMoviesActivity
}
