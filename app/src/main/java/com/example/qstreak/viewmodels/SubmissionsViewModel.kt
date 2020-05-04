package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.DailyStats
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.utils.ImageUtils
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch

class SubmissionsViewModel(
    private val submissionRepository: SubmissionRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val submissions: LiveData<List<SubmissionWithActivities>> =
        submissionRepository.submissionsWithWithActivities

    val selectedSubmission = MutableLiveData<SubmissionWithActivities>()
    val selectedSubmissionDailyStats = MutableLiveData<DailyStats>()
    val submissionDeleted = MutableLiveData<Boolean>(false)

    val selectedSubmissionScoreImage = MutableLiveData<Int>()

    private val uid: String? = sharedPrefs.getString(UID, null)

    fun selectSubmission(submissionWithActivities: SubmissionWithActivities) {
        selectedSubmission.value = submissionWithActivities
        selectedSubmissionDailyStats.value = null
        populateDailyStats(submissionWithActivities)
        selectedSubmissionScoreImage.value = ImageUtils.getImageByScore(submissionWithActivities.submission.score)
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
