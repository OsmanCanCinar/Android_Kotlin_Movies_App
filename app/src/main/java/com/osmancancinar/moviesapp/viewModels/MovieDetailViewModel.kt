package com.osmancancinar.moviesapp.viewModels

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.osmancancinar.moviesapp.R
import com.osmancancinar.moviesapp.data.Movie
import com.osmancancinar.moviesapp.data.MovieDatabase
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val app: Application) : BaseViewModel(app) {

    val movieLiveData = MutableLiveData<Movie>()
    var mInterstitialAd: InterstitialAd? = null
    val TAG = "DetailFragment"

    fun setMovieDataFromSQLite(
        movieId: Int,
        view: View,
        movieImage: ImageView,
        movieBackgroundImage: ImageView
    ) {
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()
            val movie = dao.getSelectedMovie(movieId)
            movieLiveData.value = movie

            Glide.with(view)
                .load("https://image.tmdb.org/t/p/w342${movie.poster_path}")
                .transform(CenterCrop())
                .into(movieImage)

            Glide.with(view)
                .load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
                .transform(CenterCrop())
                .into(movieBackgroundImage)
        }
    }

    fun shareData(movieTitle: String, activity: Activity) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.check_out)  + " " + movieTitle)
        intent.type = "text/plain"
        activity.startActivity(Intent.createChooser(intent, app.getString(R.string.share_to)))
    }

    fun showAd(activity: Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        }
    }

    fun callBackOfAd() {
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }

    fun loadAd() {
        //myId: ca-app-pub-2607865011970564/2509682149
        //TestId:ca-app-pub-3940256099942544/1033173712
        var adRequest = AdRequest.Builder()
            .build()
        InterstitialAd.load(app.applicationContext,"ca-app-pub-2607865011970564/2509682149", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }
}