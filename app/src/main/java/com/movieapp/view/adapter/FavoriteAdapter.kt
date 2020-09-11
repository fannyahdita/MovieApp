package com.movieapp.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movieapp.R
import com.movieapp.model.api.POSTER_BASE_URL
import com.movieapp.model.database.Favorite
import com.movieapp.view.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter (val context: Context) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private var favorite: List<Favorite> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.favorite_title.text = favorite[position].title
        holder.view.favorite_release_date.text = favorite[position].releaseDate

        val moviePosterURL = POSTER_BASE_URL + favorite[position].posterpath
        Glide.with(context)
            .load(moviePosterURL)
            .into(holder.view.imageview_favorite_poster)

        holder.view.favorite.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("id", favorite[position].movieId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favorite.size
    }

    fun setFavorite(favorites: List<Favorite>) {
        this.favorite = favorites
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)
}