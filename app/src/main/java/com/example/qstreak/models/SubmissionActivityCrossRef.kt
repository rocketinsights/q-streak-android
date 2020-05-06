package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "submission_activity_cross_ref",
    primaryKeys = ["date", "activity_slug"]
)
data class SubmissionActivityCrossRef(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "activity_slug") val activitySlug: String
)
