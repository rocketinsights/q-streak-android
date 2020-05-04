package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qstreak.models.Submission

@Dao
interface SubmissionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(submission: Submission): Long

    @Query("SELECT * FROM submissions LIMIT 100")
    fun getAllSubmissions(): LiveData<List<Submission>>

    @Update
    suspend fun update(submission: Submission)

    @Query("DELETE FROM submissions WHERE remote_id = :remoteId")
    suspend fun deleteByRemoteId(remoteId: Int)
}
