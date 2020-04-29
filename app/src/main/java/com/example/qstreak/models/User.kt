package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "zip") var zip: String,
    @ColumnInfo(name = "device_uid") var device_uid: String,
    @ColumnInfo(name = "name") var name: String
) {
    @PrimaryKey var id: Int = 1
}
