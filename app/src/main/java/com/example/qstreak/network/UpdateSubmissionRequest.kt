package com.example.qstreak.network

import com.squareup.moshi.Json

data class UpdateSubmissionRequest(
    @field:Json(name = "submission") val updateSubmissionData: UpdateSubmissionData
)

data class UpdateSubmissionData(
    @field:Json(name = "contact_count") val contactCount: Int,
    @field:Json(name = "destination_slugs") val activitySlugs: List<String>
)
