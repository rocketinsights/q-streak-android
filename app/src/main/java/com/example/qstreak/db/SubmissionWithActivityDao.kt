package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qstreak.models.SubmissionActivityCrossRef
import com.example.qstreak.models.SubmissionWithActivities

@Dao
interface SubmissionWithActivityDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(submissionActivityCrossRef: SubmissionActivityCrossRef)

    @Transaction
    @Query("SELECT * FROM submissions")
    fun getSubmissionsWithActivities(): LiveData<List<SubmissionWithActivities>>

    @Transaction
    @Query("SELECT * FROM submissions WHERE date = :date LIMIT 1")
    suspend fun getSubmissionWithActivitiesByDate(date: String): SubmissionWithActivities?

    @Query("DELETE FROM submission_activity_cross_ref WHERE remote_id = :remoteId")
    suspend fun deleteActivitiesBySubmissionId(remoteId: Int)
}
