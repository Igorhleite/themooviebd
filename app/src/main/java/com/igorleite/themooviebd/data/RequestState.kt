package com.igorleite.themooviebd.data

import okhttp3.ResponseBody
import retrofit2.Response


sealed class RequestState<out T> {
    data class ResponseSuccess<out R>(val value: R) : RequestState<R>()
    data class ResponseFailure(val error: ResponseBody?) : RequestState<Nothing>()
    data class ResponseException(val exception: Exception) : RequestState<Nothing>()
}

fun <R> Response<R>.getResponse(): RequestState<R?> {
    return try {
        when (this.isSuccessful) {
            true -> {
                RequestState.ResponseSuccess(body())
            }
            false -> {
                RequestState.ResponseFailure(errorBody())
            }
        }
    } catch (e: Exception) {
        RequestState.ResponseException(e)
    }
}