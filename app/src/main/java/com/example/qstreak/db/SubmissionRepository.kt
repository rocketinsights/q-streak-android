package com.example.qstreak.db

import androidx.lifecycle.LiveData
import com.example.qstreak.models.*
import com.example.qstreak.network.*
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import retrofit2.Retrofit

class SubmissionRepository(
    private val submissionDao: SubmissionDao,
    private val submissionWithActivityDao: SubmissionWithActivityDao,
    private val api: QstreakApiService,
    private val retrofit: Retrofit,
    private val dispatcher: CoroutineDispatcher
) {
    val submissionsWithActivities: LiveData<List<SubmissionWithActivities>> =
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

        insertSubmissionLocally(response.id, response.score, submission, activities)
    }

    private suspend fun insertSubmissionLocally(
        remoteId: Int,
        score: Int,
        submission: Submission,
        activities: List<Activity>
    ) {
        val newSubmissionId = submissionDao.insert(submission.apply {
            this.remoteId = remoteId
            this.score = score
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

    suspend fun updateSubmission(
        submission: Submission,
        activities: List<Activity>,
        uid: String
    ): ApiResult<SubmissionResponse> {
        val response = safeApiCall(dispatcher, retrofit) {
            api.updateSubmission(submission.remoteId!!,
                UpdateSubmissionRequest(
                    UpdateSubmissionData(
                        submission.contactCount,
                        activities.map { it.activitySlug }
                    )
                ),
                uid)
        }

        // For now, delete existing record and re-insert, but there's a better way to do this.
        if (response is ApiResult.Success) {
            submissionDao.deleteByRemoteId(submission.remoteId!!)
            response.data.apply {
                insertSubmissionLocally(this.id, this.score, submission, activities)
            }
        }

        return response
    }

    suspend fun getSubmissionWithActivitiesByDate(date: String): SubmissionWithActivities {
        return submissionWithActivityDao.getByDate(date)
    }

    suspend fun fetchDailyStatsForSubmission(remoteId: Int, uid: String): DailyStats {
        val api = QstreakApiService.getQstreakApiService()
        val response = api.getSubmission(remoteId, uid)

        return response.dailyStats
    }

    suspend fun delete(submission: Submission, uid: String): ApiResult<Response<Unit>> {
        val apiResponse = safeApiCall(dispatcher, retrofit) {
            api.deleteSubmission(submission.remoteId!!, uid)
        }

        if (apiResponse is ApiResult.Success) {
            submissionDao.deleteByRemoteId(submission.remoteId!!)
        }

        return apiResponse
    }

    // maybe proxy getAllSubmission too
}
