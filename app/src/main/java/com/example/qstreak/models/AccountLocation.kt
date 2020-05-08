package com.example.qstreak.models

import com.squareup.moshi.Json

data class AccountLocation(
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "county") val county: String,
    @field:Json(name = "region") val region: String,
    @field:Json(name = "region_code") val region_code: String,
    @field:Json(name = "zip_code") val zipCode: String?
)
