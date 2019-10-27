package com.vp.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.data.db.favorites.entity.FavoriteMovieEntity

class FavoritesMoviesAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<FavoritesMoviesAdapter.FavoritesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listItems = listOf<FavoriteMovieEntity>()
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(inflater.inflate(R.layout.item_favorites_list, parent, false))
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val favoriteMovieItem = listItems[position]
        if (favoriteMovieItem.poster!!.isNotEmpty() && NO_IMAGE != favoriteMovieItem.poster) {
            Glide
                .with(holder.image)
                .load(favoriteMovieItem.poster)
                .into(holder.image)
        } else {
            //ToDo: Create a core module with shared resources. Using dagger.addons for this purpose is temporary decision.
            holder.image.setImageResource(R.drawable.placeholders)
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(favoriteMovieItems: List<FavoriteMovieEntity>) {
        listItems = favoriteMovieItems
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        }
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}
