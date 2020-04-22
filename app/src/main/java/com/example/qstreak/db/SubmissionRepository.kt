package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.*
import com.example.qstreak.network.CreateSubmissionRequest
import com.example.qstreak.network.QstreakApiService
import com.example.qstreak.network.SubmissionData

class SubmissionRepository(
    private val submissionDao: SubmissionDao,
    private val submissionWithActivityDao: SubmissionWithActivityDao,
    private val api: QstreakApiService
) {
    val submissionsWithWithActivities: LiveData<List<SubmissionWithActivities>> =
        submissionWithActivityDao.getSubmissionsWithActivities()

    suspend fun insert(submission: Submission, activities: List<Activity>, uid: String) {
        // TODO convert submission model
        val response = api.createSubmission(
            CreateSubmissionRequest(
                SubmissionData(
                    submission.contactCount,
                    submission.date,
                    activities.map { it.activitySlug }
                )
            ),
            uid
        )
        val newSubmissionId = submissionDao.insert(submission.apply {
            this.remoteId = response.id
            this.score = response.score
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
        val api = QstreakApiService.getQstreakApiService()
        val response = api.getSubmission(remoteId, uid)

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
