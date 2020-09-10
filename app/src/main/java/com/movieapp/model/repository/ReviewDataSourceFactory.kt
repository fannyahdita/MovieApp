package com.movieapp.model.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.vo.Reviews
import io.reactivex.disposables.CompositeDisposable

class ReviewDataSourceFactory(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable,
    private val movieId : Int
) : DataSource.Factory<Int, Reviews>() {

    val reviewsLiveDataSource = MutableLiveData<ReviewDataSource>()

    override fun create(): DataSource<Int, Reviews> {
        val reviewDataSource = ReviewDataSource(apiService, compositeDisposable, movieId)

        reviewsLiveDataSource.postValue(reviewDataSource)
        return reviewDataSource
    }


}