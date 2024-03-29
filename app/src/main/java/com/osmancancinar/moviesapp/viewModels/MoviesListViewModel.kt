package com.osmancancinar.moviesapp.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osmancancinar.moviesapp.R
import com.osmancancinar.moviesapp.data.Movie
import com.osmancancinar.moviesapp.data.MovieDatabase
import com.osmancancinar.moviesapp.repository.MovieAPIServices
import com.osmancancinar.moviesapp.repository.MovieMapper
import com.osmancancinar.moviesapp.utilites.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MoviesListViewModel(private val app: Application) : BaseViewModel(app) {

    private val disposable = CompositeDisposable()
    private val mapper = MovieMapper()
    private val movieAPIServices = MovieAPIServices(mapper)
    private val movieObjects = MutableLiveData<List<Movie>>()
    private val customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L // 5 min in Nano Seconds
    val moviesError = MutableLiveData<Boolean>()
    val moviesLoading = MutableLiveData<Boolean>()
    val movies: LiveData<List<Movie>> get() = movieObjects

    fun refreshData(page: Int, category: String) {
        val updateTime = customPreferences.getTime()
        if ((updateTime != null) && (updateTime != 0L) && ((System.nanoTime() - updateTime) < refreshTime)) {
            getDataFromSQLite()
        } else {
            refreshFromAPI(page, category)
        }
    }

    fun refreshFromAPI(page: Int, category: String) {
        if (category == app.getString(R.string.en_arr_1)) {
            getPopularMoviesFromAPI(page)
        } else if (category == app.getString(R.string.en_arr_2)) {
            getTopRatedMoviesFromAPI(page)
        }
    }

    fun getPopularMoviesFromAPI(page: Int) {
        moviesLoading.value = true
        disposable.add(
            movieAPIServices
                .getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movies ->
                        //movieObjects.value = movies
                        storeDataInSQLite(movies)
                    },
                    { _ ->
                        moviesError.value = true
                        moviesLoading.value = false
                    }
                )
        )
    }

    fun getTopRatedMoviesFromAPI(page: Int) {
        moviesLoading.value = true
        disposable.add(
            movieAPIServices
                .getTopRatedMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movies ->
                        storeDataInSQLite(movies)
                    },
                    { _ ->
                        moviesError.value = true
                        moviesLoading.value = false
                    }
                )
        )
    }

    private fun getDataFromSQLite() {
        moviesLoading.value = true
        launch {
            val movies = MovieDatabase(getApplication()).movieDao().getAllMovies()
            showMovies(movies)
        }
    }

    private fun storeDataInSQLite(movieList: List<Movie>) {
        launch {
            val movieDao = MovieDatabase(getApplication()).movieDao()
            movieDao.deleteAll()
            val listLong = movieDao.insertAll(*movieList.toTypedArray())
            var i = 0
            while (i < movieList.size) {
                movieList[i].index = listLong[i].toInt()
                i++
            }
            showMovies(movieList)
        }
        customPreferences.saveTime(System.nanoTime())
    }

    private fun showMovies(movieList: List<Movie>) {
        movieObjects.value = movieList
        moviesError.value = false
        moviesLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}