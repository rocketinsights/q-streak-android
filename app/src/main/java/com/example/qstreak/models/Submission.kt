package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "submissions")
data class Submission(
    @PrimaryKey @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "contact_count") var contactCount: Int,
    @ColumnInfo(name = "score") var score: Int? = null
)
