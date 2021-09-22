package com.igorleite.themooviebd.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.igorleite.themooviebd.R
import com.igorleite.themooviebd.data.model.MovieModel
import com.igorleite.themooviebd.databinding.MovieItemBinding
import javax.inject.Inject

class MoviePagingAdapter @Inject constructor() :
    PagingDataAdapter<MovieModel, MoviePagingAdapter.MovieViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem.imdbID == newItem.imdbID
            }

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MoviePagingAdapter.MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviePagingAdapter.MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    inner class MovieViewHolder(private var binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MovieModel?) {
            binding.movieImage.loadImage(model?.poster)
            binding.movieTitle.text = model?.title
        }

        private fun ImageView.loadImage(imageUrl: String?) {
            if (imageUrl != "N/A") {
                Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .apply(RequestOptions().override(135, 200))
                    .into(this)
            } else {
                this.setImageResource(R.drawable.movie_image)
            }
        }
    }
}