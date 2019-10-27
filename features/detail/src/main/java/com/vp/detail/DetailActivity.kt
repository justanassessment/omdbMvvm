package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailsViewModel : DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailsViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailsViewModel
        binding.lifecycleOwner = this
        detailsViewModel.fetchDetails(getMovieId())
        detailsViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        if (menu != null) checkAndSetupFavoriteIcon(menu)
        return true
    }

    private fun checkAndSetupFavoriteIcon(menu: Menu) {
        detailsViewModel.getFavoriteMovieById(getMovieId())?.observe(this, Observer {
            favoriteMovie ->
            val isFavorite = (favoriteMovie != null)
            //ToDo: Change to findItem
            menu.getItem(0).isChecked = isFavorite
            if (isFavorite) {
                menu.getItem(0).setIcon(R.drawable.ic_star_checked)
            } else {
                menu.getItem(0).setIcon(R.drawable.ic_star)
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.star -> {
                if(item.isChecked){
                    item.isChecked = false
                    deleteFromFavoriteMovies()
                } else {
                    item.isChecked = true
                    addToFavoriteMovies()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavoriteMovies() {
        detailsViewModel.addToFavoriteMovies(getMovieId())
    }


    private fun deleteFromFavoriteMovies() {
        detailsViewModel.deleteFromFavoriteMovies(getMovieId())
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }
}
