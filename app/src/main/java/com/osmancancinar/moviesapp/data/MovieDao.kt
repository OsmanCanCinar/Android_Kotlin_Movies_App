package com.osmancancinar.moviesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert
    suspend fun insertAll(vararg movies: Movie): List<Long>

    @Query("DELETE FROM movie_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movie_table WHERE id = :movieId")
    suspend fun getSelectedMovie(movieId: Int): Movie
}