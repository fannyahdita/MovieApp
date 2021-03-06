package com.movieapp.model.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class FavoriteRepository (application: Application) {

    private var favoriteDao: FavoriteDao
    private var allFavorites: LiveData<List<Favorite>>
    private lateinit var favorite: LiveData<Favorite>

    init {
        val database: FavoriteDatabase = FavoriteDatabase.getInstance(
            application.applicationContext
        )!!
        favoriteDao = database.favoriteDao()
        allFavorites = favoriteDao.loadAllFavorite()
    }


    fun insert(favorite: Favorite) {
        InsertAsyncTask(favoriteDao).execute(favorite)
    }

    fun delete(favorite: Favorite) {
        DeleteAsyncTask(favoriteDao).execute(favorite)
    }

    fun getAllFavorites(): LiveData<List<Favorite>> {
        return allFavorites
    }

    private class InsertAsyncTask(val favoriteDao: FavoriteDao) :
        AsyncTask<Favorite, Unit, Unit>() {

        override fun doInBackground(vararg p0: Favorite?) {
            favoriteDao.insert(p0[0])
        }
    }

    private class DeleteAsyncTask(val favoriteDao: FavoriteDao) :
        AsyncTask<Favorite, Unit, Unit>() {

        override fun doInBackground(vararg p0: Favorite?) {
            p0[0]?.movieId?.let { favoriteDao.delete(it) }
        }
    }

}