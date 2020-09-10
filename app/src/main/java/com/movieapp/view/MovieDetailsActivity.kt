package com.movieapp.view

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
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.MovieDetails
import com.movieapp.view.adapter.ReviewsAdapter
import com.movieapp.viewmodel.*
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var reviewsViewModel: ReviewsViewModel

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

    private fun bindUI(it : MovieDetails) {
        textview_title.text = it.title
        textview_release_date.text = it.releaseDate
        textview_description.text = it.overview

        val posterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(posterURL)
            .into(image_poster)
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