package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class NowPlayingMoviesViewModel(private val nowPlayingMoviesRepository: NowPlayingMoviesPagedListRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  nowPlayingMoviePagedList : LiveData<PagedList<Movie>> by lazy {
        nowPlayingMoviesRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val  nowPlayingNetworkState : LiveData<NetworkState> by lazy {
        nowPlayingMoviesRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return nowPlayingMoviePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}