package com.example.qstreak.network

import com.squareup.moshi.Json

data class ApiError(@field:Json(name = "errors") val errors: Map<String, List<String>>) {
    fun getErrorString(): String {
        return errors.map { it.key.replace("_", " ") + ": " + it.value.joinToString() }
            .joinToString()
    }
}
