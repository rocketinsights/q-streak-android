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

    suspend fun createSubmission(
        contactCount: Int,
        date: String,
        activities: List<Activity>,
        uid: String
    ): ApiResult<SubmissionResponse> {
        val response = safeApiCall(dispatcher, retrofit) {
            api.createSubmission(
                CreateSubmissionRequest(
                    SubmissionData(
                        contactCount,
                        date,
                        activities.map { it.activitySlug }
                    )
                ),
                uid
            )
        }

        if (response is ApiResult.Success) {
            insertSubmissionLocally(response.data.toLocalSubmission(), response.data.destinations)
        }

        return response
    }

    suspend fun updateSubmission(
        remoteId: Int,
        contactCount: Int,
        activities: List<Activity>,
        uid: String
    ): ApiResult<SubmissionResponse> {
        val response = safeApiCall(dispatcher, retrofit) {
            api.updateSubmission(remoteId,
                UpdateSubmissionRequest(
                    UpdateSubmissionData(
                        contactCount,
                        activities.map { it.activitySlug }
                    )
                ),
                uid)
        }

        // For now, delete existing relations and re-insert, but there's a better way to do this.
        if (response is ApiResult.Success) {
            updateSubmissionLocally(response.data.toLocalSubmission(), response.data.destinations)
        }

        return response
    }

    suspend fun deleteSubmission(submission: Submission, uid: String): ApiResult<Response<Unit>> {
        val apiResponse = safeApiCall(dispatcher, retrofit) {
            api.deleteSubmission(submission.remoteId, uid)
        }

        if (apiResponse is ApiResult.Success) {
            submissionDao.deleteByRemoteId(submission.remoteId)
            submissionWithActivityDao.deleteActivitiesBySubmissionId(submission.remoteId)
        }

        return apiResponse
    }

    private suspend fun insertSubmissionLocally(
        submission: Submission,
        activities: List<Activity>
    ) {
        submissionDao.insert(submission)

        insertActivityRelations(submission.remoteId, activities)
    }

    private suspend fun updateSubmissionLocally(
        submission: Submission,
        activities: List<Activity>
    ) {
        submissionDao.update(submission)
        submissionWithActivityDao.deleteActivitiesBySubmissionId(submission.remoteId)

        insertActivityRelations(submission.remoteId, activities)
    }

    private suspend fun insertActivityRelations(remoteId: Int, activities: List<Activity>) {
        for (activity in activities) {
            submissionWithActivityDao.insert(
                SubmissionActivityCrossRef(
                    remoteId,
                    activity.activitySlug
                )
            )
        }
    }

    suspend fun getSubmissionWithActivitiesByDate(date: String): SubmissionWithActivities? {
        return submissionWithActivityDao.getSubmissionWithActivitiesByDate(date)
    }

    suspend fun fetchDailyStatsForSubmission(remoteId: Int, uid: String): DailyStats {
        val api = QstreakApiService.getQstreakApiService()
        val response = api.getSubmission(remoteId, uid)

        return response.dailyStats
    }
}
