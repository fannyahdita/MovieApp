package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.api.POST_PER_PAGE
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.repository.TopRatedMovieDataSource
import com.movieapp.model.repository.TopRatedMovieDataSourceFactory
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class TopRatedMoviesPagedListRepository (private val apiService : MovieDBInterface) {
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var topRatedMovieDataSourceFactory: TopRatedMovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        topRatedMovieDataSourceFactory = TopRatedMovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(topRatedMovieDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<TopRatedMovieDataSource, NetworkState>(
            topRatedMovieDataSourceFactory.topRatedMoviesLiveDataSource, TopRatedMovieDataSource::networkState)
    }
}