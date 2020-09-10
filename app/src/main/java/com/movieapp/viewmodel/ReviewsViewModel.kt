package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.Movie
import com.movieapp.model.vo.Reviews
import io.reactivex.disposables.CompositeDisposable

class ReviewsViewModel(private val reviewsRepository: ReviewsRepository, movieId : Int) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val reviewsList: LiveData<PagedList<Reviews>> =
        reviewsRepository.fetchLiveReviewPagedList(compositeDisposable, movieId)


    val reviewsNetworkState: LiveData<NetworkState> =
        reviewsRepository.getNetworkState()


    fun listIsEmpty(): Boolean {
        return reviewsList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}