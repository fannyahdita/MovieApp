package com.movieapp.model.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(

    @ColumnInfo(name = "movieId")
    val movieId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "releaseDate")
    val releaseDate: String,

    @ColumnInfo(name = "posterpath")
    val posterpath: String,

    @ColumnInfo(name = "overview")
    val overview: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}