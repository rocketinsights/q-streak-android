package com.example.qstreak.network

import com.squareup.moshi.Json

data class ApiError(
    @field:Json(name = "errors") val errors: Map<String, List<String>>?,
    @field:Json(name = "error") val error: String?
) {
    fun getErrorString(): String {
        return if (!errors.isNullOrEmpty()) {
            errors.map {
                it.key.replace("_", " ") + ": " + it.value.joinToString()
            }.joinToString()
        } else if (!error.isNullOrBlank()) {
            error
        } else "Something went wrong"
    }
}
