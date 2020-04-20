package com.example.qstreak.models

import com.squareup.moshi.Json

data class DailyStats(
    @field:Json(name = "cases") val cases: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "deaths") val deaths: Int,
    @field:Json(name = "risk_level") val riskLevel: Int
)
