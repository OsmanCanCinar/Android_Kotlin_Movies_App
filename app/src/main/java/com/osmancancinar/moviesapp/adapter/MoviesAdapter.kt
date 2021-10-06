package com.osmancancinar.moviesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.osmancancinar.moviesapp.R
import com.osmancancinar.moviesapp.data.Movie
import com.osmancancinar.moviesapp.databinding.MovieRowBinding
import com.osmancancinar.moviesapp.ui.MoviesListFragmentDirections
import kotlinx.android.synthetic.main.movie_row.view.*

class MoviesAdapter(private val movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(), MovieClickListener {

    class MovieViewHolder(var binding: MovieRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                /*
                movieTitle.text = movie.title
                movieRating.text = movie.vote_average.toString()
                movieIdDummyText.text = movie.id.toString()
                */
            }

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${movie.poster_path}")
                .transform(CenterCrop())
                .into(binding.movieImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.MovieViewHolder {
        val binding = DataBindingUtil.inflate<MovieRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.movie_row,
            parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesAdapter.MovieViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.binding.movie = movies[position]
        holder.binding.listener = this
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMovieList(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    override fun onMovieClicked(view: View) {
        val movie_id = view.movie_id_dummy_text.text.toString().toInt()
        val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movie_id)
        Navigation.findNavController(view).navigate(action)
    }
}