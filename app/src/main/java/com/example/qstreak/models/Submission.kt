package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "submissions")
data class Submission(
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "contact_count") var contactCount: Int,
    @ColumnInfo(name = "remote_id") var remoteId: Int? = null
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}