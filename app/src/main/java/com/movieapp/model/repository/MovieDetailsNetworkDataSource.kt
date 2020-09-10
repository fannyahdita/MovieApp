package com.movieapp.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()

    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse : LiveData<MovieDetails>
        get() = _movieDetailsResponse

    fun fetchMovieDetails(movieId : Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                    apiService.getMovieDetails(movieId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            {
                                _movieDetailsResponse.postValue(it)
                                _networkState.postValue(NetworkState.LOADED)
                            },
                            {
                                _networkState.postValue(NetworkState.ERROR)
                                it.message?.let { it1 -> Log.e("MovieDetailsDataSource", it1) }
                            }
                        )
                    )

        } catch (e : Exception) {
            Log.e("MOVIE DETAILS DATA SOURCE", e.message.toString())
        }
    }
}