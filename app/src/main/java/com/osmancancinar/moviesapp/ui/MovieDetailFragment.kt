package com.osmancancinar.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.osmancancinar.moviesapp.databinding.FragmentMovieDetailBinding
import com.osmancancinar.moviesapp.viewModels.MovieDetailViewModel

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel
    private var movie_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movie_id = MovieDetailFragmentArgs.fromBundle(it).movieId
        }

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        viewModel.getDataFromSQLite(movie_id,view,binding.movieBanner)
        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.movieLiveData.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                binding.selectedMovie = it
            }
        })
    }
}