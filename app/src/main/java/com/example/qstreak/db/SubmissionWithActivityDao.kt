package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qstreak.models.SubmissionActivitiesPair
import com.example.qstreak.models.SubmissionWithActivity

@Dao
interface SubmissionWithActivityDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(submissionWithActivity: SubmissionWithActivity)

    @Transaction
    @Query("SELECT * FROM submissions")
    fun getSubmissionsWithActivities(): LiveData<List<SubmissionActivitiesPair>>
}