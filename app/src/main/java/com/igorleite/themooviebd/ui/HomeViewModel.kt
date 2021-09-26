package com.igorleite.themooviebd.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.igorleite.themooviebd.data.model.MovieModel
import com.igorleite.themooviebd.domain.GetMoviesByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getMoviesByName: GetMoviesByName
) : ViewModel() {

    private val _movieList = MutableLiveData<PagingData<MovieModel>>()
    val movieList get() = _movieList

    fun getMoviesList(type: String, search: String) {
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 4)) {
                MoviePaging(getMoviesByName, type, search)
            }.flow.cachedIn(viewModelScope).collect {
                _movieList.value = it
            }
        }
    }
}