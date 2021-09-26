package com.igorleite.themooviebd.data.model.dto

import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("Title")
    var title: String,
    @SerializedName("Poster")
    var poster: String,
    var favorite: Boolean,
    var imdbID: String,
)