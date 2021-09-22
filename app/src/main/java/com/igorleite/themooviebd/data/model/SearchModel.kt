package com.igorleite.themooviebd.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchModel(
    @SerializedName("Search")
    var movieList: List<MovieModel> = emptyList()
) : Parcelable