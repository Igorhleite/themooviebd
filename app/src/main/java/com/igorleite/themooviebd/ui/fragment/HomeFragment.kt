package com.igorleite.themooviebd.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.igorleite.themooviebd.databinding.FragmentHomeBinding
import com.igorleite.themooviebd.domain.local.GetMovieById
import com.igorleite.themooviebd.ui.viewmodel.HomeViewModel
import com.igorleite.themooviebd.ui.adapter.MoviePagingAdapter
import com.igorleite.themooviebd.ui.adapter.paging.MovieLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var endOfSearch: Boolean = false

    @Inject
    lateinit var getMovieById: GetMovieById

    private val movieAdapter by lazy {
        MoviePagingAdapter(lifecycle, getMovieById) { movie, clickType, _ ->
            when (clickType) {
                ClickType.FAVORITE_MOVIE -> {
                    when (movie.favorite) {
                        true -> {
                            viewModel.setMovieFavorite(movie)
                        }
                        false -> {
                            viewModel.removeMovieFavorite(movie)
                        }
                    }
                }
                ClickType.OPEN_DETAIL -> {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionsListener()
        initObserver()
        initAdapterActions()
        initRecyclerView()
    }

    private fun initAdapterActions() {
        with(movieAdapter) {
        }
    }

    private fun initActionsListener() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                movieAdapter.refresh()
            }
            errorScreen.btErrorTryAgain.setOnClickListener {
                errorScreen.root.isVisible = false
                progressBar.isVisible = true
                Handler(getMainLooper()).postDelayed({
                    movieAdapter.refresh()
                }, 500)
            }
        }
    }

    private fun initObserver() {
        viewModel.movieList.observe(viewLifecycleOwner, Observer { pagingData ->
            with(binding) {
                swipeRefresh.isRefreshing = false
                with(movieAdapter) {
                    errorScreen.root.isVisible = this.itemCount == 0
                    submitData(lifecycle, pagingData)
                    launchOnLifecycleScope {
                        loadStateFlow.collectLatest { _loadStates ->
                            errorScreen.root.isVisible =
                                this.itemCount == 0 && _loadStates.refresh is LoadState.Error
                            progressBar.isVisible = _loadStates.refresh is LoadState.Loading
                            checkIfEndOfPaginationReached(_loadStates)
                        }
                    }
                }
            }
        })

    }

    private fun initRecyclerView() {
        with(binding) {
            homeRecyclerView.apply {
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = movieAdapter.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter(movieAdapter),
                    footer = MovieLoadStateAdapter(movieAdapter)
                )
            }
        }
    }

    private fun checkIfEndOfPaginationReached(loadState: CombinedLoadStates) {
        if (loadState.append.endOfPaginationReached && !endOfSearch) {
            Snackbar.make(binding.root, "End Of Search", Snackbar.LENGTH_SHORT)
                .show()
            endOfSearch = true
        }
    }

    private fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieAdapter.jobCancel()
    }

    enum class ClickType {
        OPEN_DETAIL, FAVORITE_MOVIE
    }

}