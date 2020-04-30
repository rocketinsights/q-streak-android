package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.DailyStats
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch

class SubmissionsViewModel(
    private val submissionRepository: SubmissionRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val submissions: LiveData<List<SubmissionWithActivities>> =
        submissionRepository.submissionsWithActivities

    val selectedSubmission = MutableLiveData<SubmissionWithActivities>()
    val selectedSubmissionDailyStats = MutableLiveData<DailyStats>()
    val submissionDeleted = MutableLiveData<Boolean>(false)

    private val uid: String? = sharedPrefs.getString(UID, null)

    fun selectSubmission(submissionWithActivities: SubmissionWithActivities) {
        selectedSubmission.value = submissionWithActivities
        selectedSubmissionDailyStats.value = null
        populateDailyStats(submissionWithActivities)
    }

    fun refreshSelectedSubmission() {
        val selectedDate = selectedSubmission.value?.submission?.date
        selectedDate?.let {
            viewModelScope.launch {
                selectedSubmission.value =
                    submissionRepository.getSubmissionWithActivitiesByDate(selectedDate)
            }
        }
    }

    fun deleteSubmission() {
        if (uid != null) {
            viewModelScope.launch {
                submissionRepository.delete(selectedSubmission.value!!.submission, uid)
                // TODO this is a case for SingleLiveEvent
                submissionDeleted.value = true
            }
        }
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
}
