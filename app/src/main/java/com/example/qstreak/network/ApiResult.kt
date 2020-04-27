package com.example.qstreak.network

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val apiErrors: String? = null
    ) : ApiResult<Nothing>()
}
