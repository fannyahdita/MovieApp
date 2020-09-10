package com.movieapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.movieapp.R
import com.movieapp.model.api.MovieDBClient
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.api.POSTER_BASE_URL
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.MovieDetails
import com.movieapp.viewmodel.MovieDetailsRepository
import com.movieapp.viewmodel.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var movieRepository : MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

//        val movieId = 1
        val movieId = intent.getIntExtra("id", 1)
        val apiService : MovieDBInterface = MovieDBClient.getClient()
        movieRepository =
            MovieDetailsRepository(apiService)

        movieDetailsViewModel = getViewModel(movieId)

        movieDetailsViewModel.movieDetails.observe(this, Observer{
            bindUI(it)
        })

        movieDetailsViewModel.networkState.observe(this, Observer {
            //progressbar kalau loading
            if(it == NetworkState.ERROR) {
                Toast.makeText(this, "Movie cannot be loaded", Toast.LENGTH_LONG).show()
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

    private fun getViewModel(movieId: Int) : MovieDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(
                    movieRepository,
                    movieId
                ) as T
            }
        })[MovieDetailsViewModel::class.java]
    }
}