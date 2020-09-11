package com.movieapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun loadAllFavorite(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE movieId = :movieId")
    fun loadAll(movieId: Int?): List<Favorite>

    @Insert
    fun insertFavorite(favorite: Favorite?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavorite(favorite: Favorite?)

    @Delete
    fun deleteFavorite(favorite: Favorite?)

    @Query("DELETE FROM favorite WHERE movieId = :movieId")
    fun deleteFavoriteWithId(movieId: Int)

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun loadFavoriteById(id: Int): LiveData<Favorite?>?
}