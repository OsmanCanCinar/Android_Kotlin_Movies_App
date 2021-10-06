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

    fun getPopularMovies(): Single<List<Movie>> {
        return movieAPI
            .getPopularMovies()
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