package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.Submission

class SubmissionRepository(val submissionDao: SubmissionDao) {
    val submissions: LiveData<List<Submission>> = submissionDao.getAllSubmissions()

    suspend fun insert(submission: Submission){
        submissionDao.insert(submission)
    }

    suspend fun update(submission: Submission) {
        submissionDao.update(submission)
    }

    suspend fun delete(submission: Submission) {
        submissionDao.delete(submission)
    }

    // maybe proxy getAllSubmission too
}