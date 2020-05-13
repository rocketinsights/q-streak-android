package com.example.qstreak.network

import com.squareup.moshi.Json

data class UpdateUserRequest(
    @field:Json(name = "account") val updateUserData: UpdateUserData
)

data class UpdateUserData(
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "zip") val zip: String
)
