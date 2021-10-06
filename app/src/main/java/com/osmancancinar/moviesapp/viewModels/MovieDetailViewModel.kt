package com.osmancancinar.moviesapp.viewModels

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.osmancancinar.moviesapp.data.Movie
import com.osmancancinar.moviesapp.data.MovieDatabase
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application) : BaseViewModel(application) {

    val movieLiveData = MutableLiveData<Movie>()

    fun getDataFromSQLite(movieId: Int, view: View, movieImage: ImageView) {
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()
            val movie = dao.getSelectedMovie(movieId)
            movieLiveData.value = movie

            Glide.with(view)
                .load("https://image.tmdb.org/t/p/w342${movie.poster_path}")
                .transform(CenterCrop())
                .into(movieImage)
        }
    }
}