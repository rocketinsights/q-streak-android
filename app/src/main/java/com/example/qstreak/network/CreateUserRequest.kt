package com.example.qstreak.network

import com.squareup.moshi.Json

data class CreateUserRequest(
    @field:Json(name = "account") val account: Account
)

data class Account(
    @field:Json(name = "age") val age: Int,
    @field:Json(name = "household_size") val householdSize: Int,
    @field:Json(name = "zip") val zip: String
)