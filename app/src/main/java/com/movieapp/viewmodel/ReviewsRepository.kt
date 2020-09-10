package com.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.api.POST_PER_PAGE
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.repository.ReviewDataSource
import com.movieapp.model.repository.ReviewDataSourceFactory
import com.movieapp.model.vo.Reviews
import io.reactivex.disposables.CompositeDisposable

class ReviewsRepository(private val apiService: MovieDBInterface) {

    lateinit var reviewsList: LiveData<PagedList<Reviews>>
    lateinit var reviewDataSourceFactory: ReviewDataSourceFactory

    fun fetchLiveReviewPagedList(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<PagedList<Reviews>> {
        reviewDataSourceFactory = ReviewDataSourceFactory(apiService, compositeDisposable, movieId)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        reviewsList = LivePagedListBuilder(reviewDataSourceFactory, config).build()

        return reviewsList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<ReviewDataSource, NetworkState>(
            reviewDataSourceFactory.reviewsLiveDataSource, ReviewDataSource::networkState
        )
    }
}