package com.example.qstreak.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "zip") var zip: String,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "household_size") var household_size: Int,
    @ColumnInfo(name = "device_uid") var device_uid: String
) {
    @PrimaryKey var id: Int = 1
}