package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.data.db.favorites.entity.FavoriteMovieEntity
import com.vp.favorites.viewmodel.FavoriteMoviesViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteMoviesFragment : Fragment(), FavoritesMoviesAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var favoriteMoviesViewModel: FavoriteMoviesViewModel
    private lateinit var favoritesMoviesAdapter: FavoritesMoviesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        favoriteMoviesViewModel = ViewModelProviders.of(this, factory).get(FavoriteMoviesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        initList()
        favoriteMoviesViewModel.getAllFavoriteMovies().observe(this,
            Observer<List<FavoriteMovieEntity>> { allFavoriteMovies ->
                if (allFavoriteMovies != null) {
                    favoritesMoviesAdapter.setItems(allFavoriteMovies)
                }
            })

    }

    override fun onItemClick(imdbID: String) {
        val uri = Uri.parse(DETAIL_FEATURE_DEEPLINK + DETAIL_QUERY + imdbID)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.putExtra(EXTRA_IMDB_ID, imdbID)
        intent.setPackage(requireActivity().packageName)
        startActivity(intent)
    }

    private fun initList() {
        favoritesMoviesAdapter = FavoritesMoviesAdapter(this.requireContext())
        favoritesMoviesAdapter.setOnItemClickListener(this)
        recyclerView.adapter = favoritesMoviesAdapter
        val layoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView.layoutManager = layoutManager
    }

    companion object {
        const val EXTRA_IMDB_ID = "imdbID"
        const val DETAIL_FEATURE_DEEPLINK = "app://movies/detail"
        const val DETAIL_QUERY = "?imdbID="
    }
}