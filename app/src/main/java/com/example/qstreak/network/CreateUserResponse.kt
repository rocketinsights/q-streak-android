package com.example.qstreak.network

import com.squareup.moshi.Json

data class CreateUserResponse(
    @field:Json(name = "uid") val uid: String,
    @field:Json(name = "age") val age: Int,
    @field:Json(name = "household_size") val householdSize: Int,
    @field:Json(name = "zip") val zip: String
)