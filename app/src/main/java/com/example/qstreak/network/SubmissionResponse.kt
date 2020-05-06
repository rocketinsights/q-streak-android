package com.example.qstreak.network

import com.example.qstreak.models.Activity
import com.example.qstreak.models.DailyStats
import com.example.qstreak.models.Submission
import com.squareup.moshi.Json

data class SubmissionResponse(
    @field:Json(name = "contact_count") val contactCount: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "destinations") val destinations: List<Activity>,
    @field:Json(name = "daily_stats") val dailyStats: DailyStats,
    @field:Json(name = "score") val score: Int
) {
    fun toLocalSubmission(): Submission {
        return Submission(date, contactCount, score)
    }
}
