package com.igorleite.themooviebd.ui

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.igorleite.themooviebd.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var movieAdapter: MoviePagingAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

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
        initRecyclerView()
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
                    submitData(lifecycle, pagingData)
                    launchOnLifecycleScope {
                        loadStateFlow.collectLatest { _loadStates ->
                            errorScreen.root.isVisible = _loadStates.refresh is LoadState.Error
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
        if (loadState.append.endOfPaginationReached) {
            Snackbar.make(binding.root, "End Of Search", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMoviesList("", "Marvel")
    }
}