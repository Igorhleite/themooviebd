package com.igorleite.themooviebd.ui.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.igorleite.themooviebd.R
import com.igorleite.themooviebd.databinding.LoadStateAdapterBinding
import com.igorleite.themooviebd.ui.adapter.MoviePagingAdapter

@ExperimentalPagingApi
class MovieLoadStateAdapter(
    private val adapter: MoviePagingAdapter
) : LoadStateAdapter<MovieLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(
            LoadStateAdapterBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.load_state_adapter, parent, false)
            )
        ) { adapter.retry() }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class NetworkStateItemViewHolder(
        private val binding: LoadStateAdapterBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retryCallback.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                itemView.layoutParams.let {
                    it as StaggeredGridLayoutManager.LayoutParams
                }.apply { isFullSpan = true }
                progressBar.isVisible = loadState is LoadState.Loading
                errorMsg.isVisible = loadState is LoadState.Error
                retryButton.isVisible = loadState is LoadState.Error
            }
        }
    }
}