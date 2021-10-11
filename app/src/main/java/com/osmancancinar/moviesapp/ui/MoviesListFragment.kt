package com.osmancancinar.moviesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.moviesapp.adapter.MoviesAdapter
import com.osmancancinar.moviesapp.databinding.FragmentMoviesListBinding
import com.osmancancinar.moviesapp.viewModels.MoviesListViewModel
import io.reactivex.disposables.CompositeDisposable

class MoviesListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var viewModel: MoviesListViewModel
    private  var moviesAdapter = MoviesAdapter(arrayListOf())
    private val disposable = CompositeDisposable()
    private var popularMoviesPage = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMoviesListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MoviesListViewModel::class.java)
        viewModel.refreshData(popularMoviesPage)

        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =  moviesAdapter
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.apply {
                moviesRecyclerView.visibility = View.GONE
                listErrorMsg.visibility = View.GONE
                progressBarList.visibility = View.INVISIBLE
            }
            viewModel.refreshFromAPI(popularMoviesPage)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                var value = parent?.getItemAtPosition(position)
                popularMoviesPage = value.toString().toInt()
                viewModel.refreshFromAPI(popularMoviesPage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let {
                binding.moviesRecyclerView.visibility = View.VISIBLE
                moviesAdapter.updateMovieList(movies)
            }
        } )

        viewModel.moviesError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it){
                    binding.listErrorMsg.visibility = View.VISIBLE
                } else{
                    binding.listErrorMsg.visibility = View.GONE
                }
            }
        })

        viewModel.moviesLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.progressBarList.visibility = View.VISIBLE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.listErrorMsg.visibility = View.GONE
                } else {
                    binding.progressBarList.visibility = View.GONE
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}