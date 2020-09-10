package com.movieapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.movieapp.R

class FavoriteMoviesActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movies)

        actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "Your Favorites"
        actionBar.elevation = 0F
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