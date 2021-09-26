package com.igorleite.themooviebd.data.model.dto

import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("Search")
    var movieList: List<MovieModel> = emptyList()
)