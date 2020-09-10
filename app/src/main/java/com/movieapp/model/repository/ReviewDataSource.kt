package com.movieapp.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.movieapp.model.api.FIRST_PAGE
import com.movieapp.model.api.MovieDBInterface
import com.movieapp.model.vo.Reviews
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ReviewDataSource(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable,
    private val movieId : Int
) : PageKeyedDataSource<Int, Reviews>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private var page = FIRST_PAGE


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Reviews>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieReviews(movieId, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.reviews, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("ReviewDataSourceLoadInitial", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Reviews>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieReviews(movieId, params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.reviews, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("ReviewDataSourceLoadAfter", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Reviews>) {
        TODO("Not yet implemented")
    }


}