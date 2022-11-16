package com.osmancancinar.moviesapp.repository

import com.osmancancinar.moviesapp.data.Movie
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MovieAPIServices(private val movieMapper: MovieMapper) {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val API_KEY = ""
    private lateinit var LANGUAGE: String

    private fun checkLanguage(): String {
        val currentLanguage = Locale.getDefault().language
        if (currentLanguage == "tr") {
            LANGUAGE = "tr-TR"
        } else {
            LANGUAGE = "en-US"
        }
        return LANGUAGE
    }

    private val movieAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MovieAPI::class.java)

    fun getPopularMovies(page: Int): Single<List<Movie>> {
        return movieAPI
            .getPopularMovies(API_KEY, checkLanguage(), page)
            .map { result ->
                movieMapper.mapMovieDataToMovieObjects(result.movies)
            }
    }

    fun getTopRatedMovies(page: Int): Single<List<Movie>> {
        return movieAPI
            .getTopRatedMovies(API_KEY, checkLanguage(), page)
            .map { result ->
                movieMapper.mapMovieDataToMovieObjects(result.movies)
            }
    }
}
