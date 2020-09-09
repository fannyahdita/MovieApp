package com.movieapp.model.api

import com.movieapp.model.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDBInterface {

    /**
     * 1. Popular movie : https://developers.themoviedb.org/3/movies/get-popular-movies
     * 2. Top rated : https://developers.themoviedb.org/3/movies/get-top-rated-movies
     * 3. Now Playing : https://developers.themoviedb.org/3/movies/get-now-playing
     */

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id : Int) : Single<MovieDetails>
}