package com.example.qstreak.network

import com.squareup.moshi.Json

data class CreateSubmissionResponse(
    @field:Json(name = "contact_count") val contactCount: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "destinations") val destinations: List<String>,
    @field:Json(name = "id") val id: Int
)