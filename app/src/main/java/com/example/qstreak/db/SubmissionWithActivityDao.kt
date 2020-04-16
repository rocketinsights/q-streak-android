package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.models.SubmissionActivityCrossRef

@Dao
interface SubmissionWithActivityDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(submissionActivityCrossRef: SubmissionActivityCrossRef)

    @Transaction
    @Query("SELECT * FROM submissions")
    fun getSubmissionsWithActivities(): LiveData<List<SubmissionWithActivities>>
}
