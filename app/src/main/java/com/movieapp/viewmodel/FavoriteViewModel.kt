package com.movieapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.movieapp.model.database.Favorite
import com.movieapp.model.database.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository = FavoriteRepository(application)
    private var allFavorites: LiveData<List<Favorite>> = repository.getAllFavorites()
    private lateinit var favorite: LiveData<Favorite>

    fun insert(favorite: Favorite) {
        repository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        repository.delete(favorite)
    }

    fun getAllPhotos(): LiveData<List<Favorite>> {
        return allFavorites
    }

}