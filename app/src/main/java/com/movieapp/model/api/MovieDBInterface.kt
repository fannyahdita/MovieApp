package com.movieapp.model.api

import com.movieapp.model.vo.MovieDetails
import com.movieapp.model.vo.MovieReviews
import com.movieapp.model.vo.MoviesList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {

    /**
     * 1. Popular movie : https://developers.themoviedb.org/3/movies/get-popular-movies
     * 2. Top rated : https://developers.themoviedb.org/3/movies/get-top-rated-movies
     * 3. Now Playing : https://developers.themoviedb.org/3/movies/get-now-playing
     */

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MoviesList>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("page") page: Int): Single<MoviesList>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page: Int): Single<MoviesList>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id : Int) : Single<MovieDetails>

    @GET("reviews/{movie_id}")
    fun getMovieReviews(@Path("movie_id") id : Int, @Query("page") page: Int) : Single<MovieReviews>
}