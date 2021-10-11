package com.osmancancinar.moviesapp.repository

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Single<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") api_key: String = "a384ed1c46e3eba8b7f11f883eb0b7cf",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Single<MoviesResponse>

}