package com.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.model.api.MovieDBClient
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.repository.NetworkState
import com.movieapp.popularMovies.PopularMoviesPagedListRepository
import com.movieapp.popularMovies.PopularActivityViewModel
import com.movieapp.popularMovies.PopularMoviesPagedAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularActivityViewModel

    lateinit var popularMoviesRepository: PopularMoviesPagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: MovieDBInterface = MovieDBClient.getClient()

        popularMoviesRepository = PopularMoviesPagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviesPagedAdapter(this)

        val layoutManager = LinearLayoutManager(this)

        recyclerview_popular_movies.adapter = movieAdapter
        recyclerview_popular_movies.setHasFixedSize(true)
        recyclerview_popular_movies.layoutManager = layoutManager

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressbar_main_activity.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): PopularActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularActivityViewModel(popularMoviesRepository) as T
            }
        })[PopularActivityViewModel::class.java]
    }

}