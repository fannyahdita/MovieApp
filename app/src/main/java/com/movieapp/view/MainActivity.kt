package com.movieapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.R
import com.movieapp.model.api.MovieDBClient
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.repository.NetworkState
import com.movieapp.viewmodel.PopularMoviesPagedListRepository
import com.movieapp.viewmodel.PopularMovieViewModel
import com.movieapp.view.adapter.PopularMoviesPagedAdapter
import com.movieapp.view.adapter.TopRatedMoviesPagedAdapter
import com.movieapp.viewmodel.TopRatedMovieViewModel
import com.movieapp.viewmodel.TopRatedMoviesPagedListRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var popularMovieViewModel: PopularMovieViewModel
    private lateinit var topRatedMovieViewModel: TopRatedMovieViewModel

    lateinit var popularMoviesRepository: PopularMoviesPagedListRepository
    lateinit var topRatedMoviesRepository: TopRatedMoviesPagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: MovieDBInterface = MovieDBClient.getClient()

        popularMoviesRepository = PopularMoviesPagedListRepository(apiService)
        topRatedMoviesRepository = TopRatedMoviesPagedListRepository(apiService)

        popularMovieViewModel = getPopularMovieViewModel()
        topRatedMovieViewModel = getTopRatedMovieViewModel()

        val popularMovieAdapter = PopularMoviesPagedAdapter(this)
        val topRatedMovieAdapter = TopRatedMoviesPagedAdapter(this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerview_popular_movies.adapter = popularMovieAdapter
        recyclerview_popular_movies.setHasFixedSize(true)
        recyclerview_popular_movies.layoutManager = layoutManager

        popularMovieViewModel.popularMoviePagedList.observe(this, Observer {
            popularMovieAdapter.submitList(it)
        })

        popularMovieViewModel.popularMovieNetworkState.observe(this, Observer {
            progressbar_main_activity.visibility =
                if (popularMovieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!popularMovieViewModel.listIsEmpty()) {
                popularMovieAdapter.setNetworkState(it)
            }
        })

        val topRatedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_toprated_movies.adapter = topRatedMovieAdapter
        recyclerview_toprated_movies.setHasFixedSize(true)
        recyclerview_toprated_movies.layoutManager = topRatedLayoutManager

        topRatedMovieViewModel.topRatedMoviePagedList.observe(this, Observer {
            topRatedMovieAdapter.submitList(it)
        })

        topRatedMovieViewModel.topRatedNetworkState.observe(this, Observer {
            progressbar_main_activity.visibility =
                if (topRatedMovieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!topRatedMovieViewModel.listIsEmpty()) {
                topRatedMovieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getPopularMovieViewModel(): PopularMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularMovieViewModel(
                    popularMoviesRepository
                ) as T
            }
        })[PopularMovieViewModel::class.java]
    }

    private fun getTopRatedMovieViewModel(): TopRatedMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TopRatedMovieViewModel(
                    topRatedMoviesRepository
                ) as T
            }
        })[TopRatedMovieViewModel::class.java]
    }

}