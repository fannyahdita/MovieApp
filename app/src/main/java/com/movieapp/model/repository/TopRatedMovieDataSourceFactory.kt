package com.movieapp.model.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class TopRatedMovieDataSourceFactory (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val topRatedMoviesLiveDataSource =  MutableLiveData<TopRatedMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = TopRatedMovieDataSource(apiService,compositeDisposable)

        topRatedMoviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}