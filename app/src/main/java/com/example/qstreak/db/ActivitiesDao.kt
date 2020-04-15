package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.qstreak.models.Activity

@Dao
interface ActivitiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActivities(activities: List<Activity>)

    @Query("SELECT * FROM activities LIMIT 100")
    fun getAllActivities(): LiveData<List<Activity>>
}