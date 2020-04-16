package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.*
import com.example.qstreak.network.CreateSubmissionRequest
import com.example.qstreak.network.QstreakApiService
import com.example.qstreak.network.SubmissionData

class SubmissionRepository(
    private val submissionDao: SubmissionDao,
    private val submissionWithActivityDao: SubmissionWithActivityDao
) {
    val submissionsWithWithActivities: LiveData<List<SubmissionWithActivities>> =
        submissionWithActivityDao.getSubmissionsWithActivities()

    suspend fun insert(submission: Submission, activities: List<Activity>, uid: String) {
        val api = QstreakApiService.getQstreakApiService(uid)

        // TODO convert submission model
        val response = api.createSubmission(
            CreateSubmissionRequest(
                SubmissionData(
                    submission.contactCount,
                    submission.date,
                    activities.map { it.activitySlug }
                )
            )
        )
        val newSubmissionId = submissionDao.insert(submission.apply {
            this.remoteId = response.id
        })

        for (activity in activities) {
            submissionWithActivityDao.insert(
                SubmissionActivityCrossRef(
                    newSubmissionId.toInt(),
                    activity.activitySlug
                )
            )
        }
    }

    suspend fun getSubmissionById(id: Int): Submission {
        return submissionDao.get(id)
    }

    suspend fun fetchDailyStatsForSubmission(remoteId: Int, uid: String): DailyStats {
        val api = QstreakApiService.getQstreakApiService(uid)
        val response = api.getSubmission(remoteId)

        return response.dailyStats
    }

    suspend fun update(submission: Submission) {
        submissionDao.update(submission)
    }

    suspend fun delete(submission: Submission) {
        submissionDao.delete(submission)
    }

    // maybe proxy getAllSubmission too
}
