package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class TopRatedMoviesViewModel(private val topRatedMoviesRepository: TopRatedMoviesPagedListRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val topRatedMoviePagedList: LiveData<PagedList<Movie>> =
        topRatedMoviesRepository.fetchLiveMoviePagedList(compositeDisposable)


    val topRatedNetworkState: LiveData<NetworkState> =
        topRatedMoviesRepository.getNetworkState()


    fun listIsEmpty(): Boolean {
        return topRatedMoviePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}