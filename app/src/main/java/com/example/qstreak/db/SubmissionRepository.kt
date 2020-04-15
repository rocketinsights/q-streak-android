package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.Activity
import com.example.qstreak.models.Submission
import com.example.qstreak.models.SubmissionActivitiesPair
import com.example.qstreak.models.SubmissionWithActivity
import com.example.qstreak.network.CreateSubmissionRequest
import com.example.qstreak.network.QstreakApiService
import com.example.qstreak.network.SubmissionData

class SubmissionRepository(
    private val submissionDao: SubmissionDao,
    private val submissionWithActivityDao: SubmissionWithActivityDao
) {
    val submissionsWithActivities: LiveData<List<SubmissionActivitiesPair>> =
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
            // TODO handle object result rather than converting to string
            this.dailyStats = response.dailyStats.toString()
        })

        for (activity in activities) {
            submissionWithActivityDao.insert(
                SubmissionWithActivity(
                    newSubmissionId.toInt(),
                    activity.activitySlug
                )
            )
        }
    }

    suspend fun getSubmissionById(id: Int): Submission {
        return submissionDao.get(id)
    }

    suspend fun update(submission: Submission) {
        submissionDao.update(submission)
    }

    suspend fun delete(submission: Submission) {
        submissionDao.delete(submission)
    }

    // maybe proxy getAllSubmission too
}