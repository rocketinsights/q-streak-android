package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["submission_id", "activity_slug"])
data class SubmissionActivityCrossRef(
    @ColumnInfo(name = "submission_id") val submissionId: Int,
    @ColumnInfo(name = "activity_slug") val activitySlug: String
)
