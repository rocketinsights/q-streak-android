package com.example.qstreak.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qstreak.models.Submission

@Dao
interface SubmissionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(submission: Submission)

    @Query("SELECT * FROM submissions LIMIT 100")
    fun getAllSubmissions(): LiveData<List<Submission>>

    @Update
    suspend fun update(submission: Submission)

    @Delete
    suspend fun delete(submission: Submission)
}