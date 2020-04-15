package com.example.qstreak.network

import com.example.qstreak.models.Activity
import com.squareup.moshi.Json

data class CreateSubmissionResponse(
    @field:Json(name = "contact_count") val contactCount: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "destinations") val destinations: List<Activity>,
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "daily_stats") val dailyStats: DailyStats?
)

data class DailyStats(
    @field:Json(name = "cases") val cases: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "deaths") val deaths: Int
)