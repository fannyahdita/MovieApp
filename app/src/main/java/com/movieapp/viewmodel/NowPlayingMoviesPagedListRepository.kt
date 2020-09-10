package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.api.POST_PER_PAGE
import com.movieapp.model.repository.*
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class NowPlayingMoviesPagedListRepository (private val apiService : MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var nowPlayingMoviesDataSourceFactory: NowPlayingMovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        nowPlayingMoviesDataSourceFactory = NowPlayingMovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(nowPlayingMoviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<NowPlayingMovieDataSource, NetworkState>(
            nowPlayingMoviesDataSourceFactory.nowPlayingMoviesLiveDataSource, NowPlayingMovieDataSource::networkState)
    }
}