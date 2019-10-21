package com.vp.favorites.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vp.daggeraddons.DaggerViewModelFactory
import com.vp.daggeraddons.ViewModelKey
import com.vp.favorites.FavoriteMoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class FavoriteMoviesViewModelModule {

    @Binds
    abstract fun bindDaggerViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMoviesViewModel::class)
    abstract fun bindFavoriteViewModel(favoriteListMoviesViewModel: FavoriteMoviesViewModel): ViewModel
}
