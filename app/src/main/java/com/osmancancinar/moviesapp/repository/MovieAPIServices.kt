package com.osmancancinar.moviesapp.repository

import com.osmancancinar.moviesapp.data.Movie
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MovieAPIServices(
    private val movieMapper: MovieMapper
) {

    private val movieAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MovieAPI::class.java)

    fun getPopularMovies(page: Int): Single<List<Movie>> {
        return movieAPI
            .getPopularMovies("a384ed1c46e3eba8b7f11f883eb0b7cf","en-US",page)
            .map { result ->
                movieMapper.mapMovieDataToMovieObjects(result.movies)
            }
    }

    fun getTopRatedMovies(): Single<List<Movie>> {
        return movieAPI
            .getTopRatedMovies()
            .map { result ->
                movieMapper.mapMovieDataToMovieObjects(result.movies)
            }
    }
}