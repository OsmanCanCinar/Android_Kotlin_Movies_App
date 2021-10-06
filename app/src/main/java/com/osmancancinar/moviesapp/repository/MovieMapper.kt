package com.osmancancinar.moviesapp.repository

import com.osmancancinar.moviesapp.data.Movie

class MovieMapper {

    fun mapMovieDataToMovieObjects(movieList: List<MovieData>): List<Movie> {
        return movieList.map { mapDataToObject(it) }
    }

    fun mapDataToObject(movie: MovieData): Movie {
        return Movie(
            movie.id,
            movie.title,
            movie.overview,
            movie.poster_path,
            movie.backdropPath,
            movie.vote_average,
            movie.release_date
        )
    }
}