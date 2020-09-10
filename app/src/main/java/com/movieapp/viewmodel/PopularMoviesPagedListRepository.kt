package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.api.POST_PER_PAGE
import com.movieapp.model.repository.PopularMovieDataSource
import com.movieapp.model.repository.PopularMovieDataSourceFactory
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesPagedListRepository (private val apiService : MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var popularMoviesDataSourceFactory: PopularMovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        popularMoviesDataSourceFactory = PopularMovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(popularMoviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<PopularMovieDataSource, NetworkState>(
            popularMoviesDataSourceFactory.popularMoviesLiveDataSource, PopularMovieDataSource::networkState)
    }
}