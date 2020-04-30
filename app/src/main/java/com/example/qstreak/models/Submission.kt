package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "submissions")
data class Submission(
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "contact_count") var contactCount: Int,
    @PrimaryKey @ColumnInfo(name = "remote_id") var remoteId: Int,
    @ColumnInfo(name = "score") var score: Int? = null
)
