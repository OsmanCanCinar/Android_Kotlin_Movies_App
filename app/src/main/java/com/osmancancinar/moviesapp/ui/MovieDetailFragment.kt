package com.osmancancinar.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.MobileAds
import com.osmancancinar.moviesapp.databinding.FragmentMovieDetailBinding
import com.osmancancinar.moviesapp.viewModels.MovieDetailViewModel

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel
    private var movieId = 0
    private var movieTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(requireActivity()) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movieId = MovieDetailFragmentArgs.fromBundle(it).movieId
            movieTitle = MovieDetailFragmentArgs.fromBundle(it).movieTitle
        }

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        viewModel.setMovieDataFromSQLite(
            movieId,
            view,
            binding.movieBanner,
            binding.movieBackground
        )
        observeLiveData()

        viewModel.loadAd()
        viewModel.callBackOfAd()

        binding.fabShare.setOnClickListener {
            viewModel.shareData(movieTitle, requireActivity())
            viewModel.showAd(requireActivity())
        }
    }

    private fun observeLiveData() {
        viewModel.movieLiveData.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                binding.selectedMovie = it
            }
        })
    }
}