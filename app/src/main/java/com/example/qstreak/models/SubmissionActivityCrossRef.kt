package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "submission_activity_cross_ref",
    primaryKeys = ["remote_id", "activity_slug"]
)
data class SubmissionActivityCrossRef(
    @ColumnInfo(name = "remote_id") val remoteId: Int,
    @ColumnInfo(name = "activity_slug") val activitySlug: String
)
