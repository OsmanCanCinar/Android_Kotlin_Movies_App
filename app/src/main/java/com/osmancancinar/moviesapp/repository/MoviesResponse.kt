package com.osmancancinar.moviesapp.repository

import com.google.gson.annotations.SerializedName

data class MoviesResponse(

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val movies: List<MovieData>,

    @SerializedName("total_pages")
    val pages: Int
)