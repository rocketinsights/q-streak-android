package com.example.qstreak.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Activity
import com.example.qstreak.models.DailyStats
import com.example.qstreak.models.Submission
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class SubmissionsViewModel(
    app: Application,
    private val submissionRepository: SubmissionRepository,
    private val activitiesRepository: ActivitiesRepository
) : AndroidViewModel(app) {

    val submissions: LiveData<List<SubmissionWithActivities>> =
        submissionRepository.submissionsWithWithActivities
    val activities: LiveData<List<Activity>> = activitiesRepository.activities

    val selectedSubmission = MutableLiveData<SubmissionWithActivities>()
    val selectedSubmissionDailyStats = MutableLiveData<DailyStats>()

    val newSubmissionDate = MutableLiveData<Date>(Date())

    private val checkedActivities = arrayListOf<Activity>()
    private val uid: String? = EncryptedSharedPreferencesUtil.getUidAsBearerToken(app)

    init {
        refreshActivities()
    }

    fun selectSubmission(submissionWithActivities: SubmissionWithActivities) {
        selectedSubmission.value = submissionWithActivities
        selectedSubmissionDailyStats.value = null
        populateDailyStats(submissionWithActivities)
    }

    private fun populateDailyStats(submissionWithActivities: SubmissionWithActivities) {
        // TODO handle null uid
        if (uid != null) {
            viewModelScope.launch {
                submissionWithActivities.submission.remoteId?.let {
                    val response = submissionRepository.fetchDailyStatsForSubmission(it, uid)
                    selectedSubmissionDailyStats.value = response
                }
            }
        }
    }

    fun createSubmission(submission: Submission) {
        // TODO handle null uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    submissionRepository.insert(submission, checkedActivities, uid)
                } catch (e: Exception) {
                    // TODO retrieve error text from response body to surface to user (at api layer)
                    Timber.e("Error message: %s", e.message)
                } finally {
                    checkedActivities.clear()
                }
            }
        }
    }

    fun onActivityCheckboxToggled(activity: Activity) {
        if (checkedActivities.contains(activity)) {
            checkedActivities.remove(activity)
        } else {
            checkedActivities.add(activity)
        }
    }

    private fun refreshActivities() {
        // TODO handle null uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    activitiesRepository.refreshActivities(uid)
                } catch (e: Exception) {
                    Timber.e("Error message: %s", e.message)
                }
            }
        }
    }
}
