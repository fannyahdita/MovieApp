package com.movieapp.model.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class PopularMovieDataSourceFactory (private val apiService : MovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<PopularMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = PopularMovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}