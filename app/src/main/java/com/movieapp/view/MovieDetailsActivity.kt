package com.movieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.movieapp.R
import com.movieapp.model.api.MovieDBClient
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.api.POSTER_BASE_URL
import com.movieapp.model.database.Favorite
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.MovieDetails
import com.movieapp.view.adapter.ReviewsAdapter
import com.movieapp.viewmodel.*
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var reviewsViewModel: ReviewsViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var favoriteList: List<Favorite>

    private lateinit var movieRepository : MovieDetailsRepository
    private lateinit var reviewsRepository: ReviewsRepository

    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movieId = intent.getIntExtra("id", 1)

        actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = ""
        actionBar.elevation = 0F

        val apiService : MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)
        reviewsRepository = ReviewsRepository(apiService)

        movieDetailsViewModel = getMovieDetailsViewModel(movieId)
        reviewsViewModel = getReviewsViewModel(movieId)
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)

        favoriteViewModel.getAllFavorites().observe(this, Observer {
            favoriteList = it
        })

        val reviewsAdapter = ReviewsAdapter(this)

        movieDetailsViewModel.movieDetails.observe(this, Observer{
            bindUI(it)
        })

        movieDetailsViewModel.networkState.observe(this, Observer {
            //progressbar kalau loading
            if(it == NetworkState.ERROR) {
                Toast.makeText(this, "Movie cannot be loaded", Toast.LENGTH_LONG).show()
            }
        })

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerview_reviews.layoutManager = layoutManager
        recyclerview_reviews.setHasFixedSize(true)
        recyclerview_reviews.adapter = reviewsAdapter

        reviewsViewModel.reviewsList.observe(this, Observer {
            reviewsAdapter.submitList(it)
        })

        reviewsViewModel.reviewsNetworkState.observe(this, Observer {
            if (!reviewsViewModel.listIsEmpty()) {
                reviewsAdapter.setNetworkState(it)
            }
        })
    }

    private fun bindUI(details : MovieDetails) {
        textview_title.text = details.title
        textview_release_date.text = details.releaseDate
        textview_description.text = details.overview

        val posterURL = POSTER_BASE_URL + details.posterPath
        Glide.with(this)
            .load(posterURL)
            .into(image_poster)

        button_share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareBody = "Try to watch ${details.title} now! Checkout on https://www.themoviedb.org/movie/"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareIntent, "Share Via"))
        }

        for (favorite in favoriteList) {
            if(favorite.movieId == details.id) {
                button_favorite.setImageResource(R.drawable.already_favorited_button)
            }
        }

        button_favorite.setOnClickListener {
            val favorite = Favorite (details.id, details.title, details.releaseDate, details.posterPath, details.overview)
            if (button_favorite.tag == R.drawable.already_favorited_button) {
                button_favorite.setImageResource(R.drawable.not_favorite)
                favoriteViewModel.delete(favorite)
                Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
            } else {
                button_favorite.setImageResource(R.drawable.already_favorited_button)
                favoriteViewModel.insert(favorite)
                Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getMovieDetailsViewModel(movieId: Int) : MovieDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(
                    movieRepository,
                    movieId
                ) as T
            }
        })[MovieDetailsViewModel::class.java]
    }

    private fun getReviewsViewModel(movieId: Int): ReviewsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ReviewsViewModel(
                    reviewsRepository,
                    movieId
                ) as T
            }
        })[ReviewsViewModel::class.java]
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