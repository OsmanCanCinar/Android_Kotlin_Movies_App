package com.osmancancinar.moviesapp.repository

import com.google.gson.annotations.SerializedName

data class MovieData(

    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("vote_average")
    val vote_average: Float,

    @SerializedName("release_date")
    val release_date: String,

    @SerializedName("original_language")
    val original_language: String
)