package com.movieapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun loadAllFavorite(): LiveData<List<Favorite>>

    @Insert
    fun insert(favorite: Favorite?)

    @Query("DELETE FROM favorite WHERE movieId = :movieId")
    fun delete(movieId: Int)

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun loadFavoriteById(id: Int): LiveData<Favorite?>?
}