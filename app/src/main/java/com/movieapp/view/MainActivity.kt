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
import com.movieapp.view.adapter.NowPlayingMoviesPagedAdapter
import com.movieapp.view.adapter.PopularMoviesPagedAdapter
import com.movieapp.view.adapter.TopRatedMoviesPagedAdapter
import com.movieapp.viewmodel.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var popularMoviesViewModel: PopularMoviesViewModel
    private lateinit var topRatedMoviesViewModel: TopRatedMoviesViewModel
    private lateinit var nowPlayingMoviesViewModel: NowPlayingMoviesViewModel

    lateinit var popularMoviesRepository: PopularMoviesPagedListRepository
    lateinit var topRatedMoviesRepository: TopRatedMoviesPagedListRepository
    lateinit var nowPlayingMoviesRepository: NowPlayingMoviesPagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: MovieDBInterface = MovieDBClient.getClient()

        popularMoviesRepository = PopularMoviesPagedListRepository(apiService)
        topRatedMoviesRepository = TopRatedMoviesPagedListRepository(apiService)
        nowPlayingMoviesRepository = NowPlayingMoviesPagedListRepository(apiService)

        popularMoviesViewModel = getPopularMovieViewModel()
        topRatedMoviesViewModel = getTopRatedMovieViewModel()
        nowPlayingMoviesViewModel = getNowPlayingMovieViewModel()

        val popularMovieAdapter = PopularMoviesPagedAdapter(this)
        val topRatedMovieAdapter = TopRatedMoviesPagedAdapter(this)
        val nowPlayingMovieAdapter = NowPlayingMoviesPagedAdapter(this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerview_popular_movies.adapter = popularMovieAdapter
        recyclerview_popular_movies.setHasFixedSize(true)
        recyclerview_popular_movies.layoutManager = layoutManager

        popularMoviesViewModel.popularMoviePagedList.observe(this, Observer {
            popularMovieAdapter.submitList(it)
        })

        popularMoviesViewModel.popularMovieNetworkState.observe(this, Observer {
            progressbar_main_activity.visibility =
                if (popularMoviesViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!popularMoviesViewModel.listIsEmpty()) {
                popularMovieAdapter.setNetworkState(it)
            }
        })

        val topRatedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_toprated_movies.adapter = topRatedMovieAdapter
        recyclerview_toprated_movies.setHasFixedSize(true)
        recyclerview_toprated_movies.layoutManager = topRatedLayoutManager

        topRatedMoviesViewModel.topRatedMoviePagedList.observe(this, Observer {
            topRatedMovieAdapter.submitList(it)
        })

        topRatedMoviesViewModel.topRatedNetworkState.observe(this, Observer {
            progressbar_main_activity.visibility =
                if (topRatedMoviesViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!topRatedMoviesViewModel.listIsEmpty()) {
                topRatedMovieAdapter.setNetworkState(it)
            }
        })

        val nowPlayingLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_nowplaying_movies.adapter = nowPlayingMovieAdapter
        recyclerview_nowplaying_movies.setHasFixedSize(true)
        recyclerview_nowplaying_movies.layoutManager = nowPlayingLayoutManager

        nowPlayingMoviesViewModel.nowPlayingMoviePagedList.observe(this, Observer {
            nowPlayingMovieAdapter.submitList(it)
        })

        nowPlayingMoviesViewModel.nowPlayingNetworkState.observe(this, Observer {
            progressbar_main_activity.visibility =
                if (nowPlayingMoviesViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!nowPlayingMoviesViewModel.listIsEmpty()) {
                nowPlayingMovieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getPopularMovieViewModel(): PopularMoviesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularMoviesViewModel(
                    popularMoviesRepository
                ) as T
            }
        })[PopularMoviesViewModel::class.java]
    }

    private fun getTopRatedMovieViewModel(): TopRatedMoviesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TopRatedMoviesViewModel(
                    topRatedMoviesRepository
                ) as T
            }
        })[TopRatedMoviesViewModel::class.java]
    }

    private fun getNowPlayingMovieViewModel(): NowPlayingMoviesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return NowPlayingMoviesViewModel(
                    nowPlayingMoviesRepository
                ) as T
            }
        })[NowPlayingMoviesViewModel::class.java]
    }

}