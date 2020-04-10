package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.Submission
import com.example.qstreak.network.CreateSubmissionRequest
import com.example.qstreak.network.QstreakApiService

class SubmissionRepository(
    private val submissionDao: SubmissionDao
) {
    val submissions: LiveData<List<Submission>> = submissionDao.getAllSubmissions()

    suspend fun insert(submission: Submission, uid: String) {
        val api = QstreakApiService.getQstreakApiService(uid)

        // TODO convert submission model
        val response = api.createSubmission(
            CreateSubmissionRequest(
                com.example.qstreak.network.Submission(
                    submission.contactCount,
                    submission.date,
                    emptyList()
                )
            )
        )
        submissionDao.insert(submission.apply { this.remoteId = response.id })
    }

    suspend fun update(submission: Submission) {
        submissionDao.update(submission)
    }

    suspend fun delete(submission: Submission) {
        submissionDao.delete(submission)
    }

    // maybe proxy getAllSubmission too
}