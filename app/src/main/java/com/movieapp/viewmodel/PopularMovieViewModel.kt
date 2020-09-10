package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class PopularMovieViewModel (private val popularMoviesRepository : PopularMoviesPagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  popularMoviePagedList : LiveData<PagedList<Movie>> by lazy {
        popularMoviesRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val  popularMovieNetworkState : LiveData<NetworkState> by lazy {
        popularMoviesRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return popularMoviePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}