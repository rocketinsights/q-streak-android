package com.example.qstreak.network

import com.squareup.moshi.Json

data class CreateUserRequest(
    @field:Json(name = "account") val account: Account
)

data class Account(
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "zip") val zip: String
)
