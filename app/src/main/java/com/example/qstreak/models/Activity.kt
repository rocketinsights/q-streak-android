package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "activities",
    indices = [Index(value = ["activity_slug"], name = "activity_slug")]
)
data class Activity(
    @field:Json(name = "contact_count") @ColumnInfo(name = "contact_count") val contactCount: Int,
    @field:Json(name = "name") @ColumnInfo(name = "name") val name: String,
    @field:Json(name = "slug") @ColumnInfo(name = "activity_slug") @PrimaryKey val activitySlug: String,
    @field:Json(name = "icon") @ColumnInfo(name = "icon") val icon: String
)