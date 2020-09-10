package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel (private val movieRepository: MovieDetailsRepository, movieId: Int) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> = movieRepository.fetchingSingleMovieDetails(compositeDisposable, movieId)
    val networkState : LiveData<NetworkState> = movieRepository.getMovieDetailsNetworkState()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}