package com.movieapp.movieDetails

import androidx.lifecycle.LiveData
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.repository.MovieDetailsNetworkDataSource
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : MovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource : MovieDetailsNetworkDataSource

    fun fetchingSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.movieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}