package com.movieapp.model.vo


import com.google.gson.annotations.SerializedName

data class MovieReviews(
    val id: Int,
    val page: Int,
    val reviews: List<Reviews>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)