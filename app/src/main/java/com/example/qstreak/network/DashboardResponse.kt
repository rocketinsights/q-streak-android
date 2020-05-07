package com.example.qstreak.network

import com.example.qstreak.models.DailyStats
import com.squareup.moshi.Json

data class DashboardResponse(
    @field:Json(name = "daily_stats") val dailyStats: DailyStats,
    @field:Json(name = "messages") val messages: List<DashboardMessage>
)

data class DashboardMessage(
    @field:Json(name = "title") val dashboardMessageTitle: String,
    @field:Json(name = "body") val dashboardMessageBody: String,
    @field:Json(name = "icon") val icon: String,
    @field:Json(name = "url") val dashboardMessageUrl: String?
)
