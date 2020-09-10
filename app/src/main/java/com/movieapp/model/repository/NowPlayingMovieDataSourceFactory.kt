package com.movieapp.model.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class NowPlayingMovieDataSourceFactory (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val nowPlayingMoviesLiveDataSource =  MutableLiveData<NowPlayingMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = NowPlayingMovieDataSource(apiService,compositeDisposable)

        nowPlayingMoviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}