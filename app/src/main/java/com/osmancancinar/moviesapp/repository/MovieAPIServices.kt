package com.osmancancinar.moviesapp.repository

import com.osmancancinar.moviesapp.data.Movie
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieAPIServices(
    private val movieMapper: MovieMapper
) {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val API_KEY = "a384ed1c46e3eba8b7f11f883eb0b7cf"
    private val LANGUAGE = "en-US"


    private val movieAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MovieAPI::class.java)

    fun getPopularMovies(page: Int): Single<List<Movie>> {
        return movieAPI
            .getPopularMovies(API_KEY, LANGUAGE, page)
            .map { result ->
                movieMapper.mapMovieDataToMovieObjects(result.movies)
            }
    }

    fun getTopRatedMovies(page: Int): Single<List<Movie>> {
        return movieAPI
            .getTopRatedMovies(API_KEY, LANGUAGE, page)
            .map { result ->
                movieMapper.mapMovieDataToMovieObjects(result.movies)
            }
    }
}