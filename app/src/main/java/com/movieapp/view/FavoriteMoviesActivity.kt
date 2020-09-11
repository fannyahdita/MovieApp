package com.movieapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.R
import com.movieapp.view.adapter.FavoriteAdapter
import com.movieapp.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_favorite_movies.*

class FavoriteMoviesActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoritAdapter : FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movies)

        actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Your Favorites"
        actionBar.elevation = 0F

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        favoritAdapter = FavoriteAdapter(this)

        val layoutManager = LinearLayoutManager(this)
        recylerview_favorite.adapter = favoritAdapter
        recylerview_favorite.layoutManager = layoutManager

        getViewModel()
    }

    fun getViewModel() {
        favoriteViewModel.getAllFavorites().observe(this, Observer{
            favoritAdapter.setFavorite(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}