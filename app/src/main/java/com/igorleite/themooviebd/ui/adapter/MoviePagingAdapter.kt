package com.igorleite.themooviebd.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.igorleite.themooviebd.R
import com.igorleite.themooviebd.data.RequestState
import com.igorleite.themooviebd.data.model.Movie
import com.igorleite.themooviebd.databinding.MovieItemBinding
import com.igorleite.themooviebd.domain.local.GetMovieById
import com.igorleite.themooviebd.ui.fragment.HomeFragment.ClickType
import com.igorleite.themooviebd.ui.adapter.MoviePagingAdapter.MovieViewHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class MoviePagingAdapter(
    private val lifecycle: Lifecycle,
    private val getFavorite: GetMovieById,
    private val onClickListener: (movie: Movie, clickType: ClickType, position: Int) -> Unit
) :
    PagingDataAdapter<Movie, MovieViewHolder>(DIFF_UTIL) {
    private var job: Job? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.imdbID == newItem.imdbID
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { _movie ->
            holder.bind(_movie)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onClickListener)
    }

    inner class MovieViewHolder(
        private var binding: MovieItemBinding,
        private val onClickListener: (movie: Movie, clickType: ClickType, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                movieFavoriteCheckBox.isFavorite(movie)
                movieImage.loadImage(movie.poster)
                movieTitle.text = movie.title
                movieFavoriteCheckBox.setOnClickListener {
                    movie.favorite = movieFavoriteCheckBox.isChecked
                    onClickListener.invoke(movie, ClickType.FAVORITE_MOVIE, layoutPosition)
                }
            }
        }

        private fun ImageView.loadImage(imageUrl: String?) {
            if (imageUrl != "N/A") {
                Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .apply(RequestOptions().override(135, 200))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
            } else {
                this.setImageResource(R.drawable.movie_image)
            }
        }

        private fun CheckBox.isFavorite(movie: Movie) {
            job = lifecycle.coroutineScope.launch {
                val dataState = getFavorite.run(GetMovieById.Params(movie))
                isChecked = dataState is RequestState.ResponseSuccess && dataState.value.favorite
            }
        }
    }

    fun jobCancel() {
        job?.cancel()
    }
}