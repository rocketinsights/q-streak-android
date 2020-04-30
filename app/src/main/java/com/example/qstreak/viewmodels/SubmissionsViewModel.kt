package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.DailyLogItemInfo
import com.example.qstreak.models.DailyStats
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SubmissionsViewModel(
    private val submissionRepository: SubmissionRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val submissions: LiveData<List<SubmissionWithActivities>> =
        submissionRepository.submissionsWithActivities

    val dailyLogInfos = MutableLiveData<List<DailyLogItemInfo>>()

    val selectedSubmission = MutableLiveData<SubmissionWithActivities>()
    val selectedSubmissionDailyStats = MutableLiveData<DailyStats>()
    val submissionDeleted = MutableLiveData<Boolean>(false)

    private val uid: String? = sharedPrefs.getString(UID, null)

    // TODO extract date handling into a utility class
    fun generateDailyLogInfos() {
        val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val calendar = Calendar.getInstance().apply {
            this.add(Calendar.DATE, -14)
        }

        val dates = mutableListOf<Date>().apply {
            for (i in 1..14) {
                this.add(calendar.time)
                calendar.add(Calendar.DATE, 1)
            }
        }

        val dailyInfos = arrayListOf<DailyLogItemInfo>()

        viewModelScope.launch {
            for (date in dates) {
                val submissionOrNull =
                    submissionRepository.getSubmissionWithActivitiesByDate(
                        fullDateFormat.format(
                            date
                        )
                    )
                dailyInfos.add(DailyLogItemInfo(date, submissionOrNull))
            }
            dailyInfos.sortBy { it.date }

            dailyLogInfos.value = dailyInfos
        }
    }

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
                submissionRepository.deleteSubmission(selectedSubmission.value!!.submission, uid)
                // TODO this is a case for SingleLiveEvent
                submissionDeleted.value = true
            }
        }
    }

    private fun populateDailyStats(submissionWithActivities: SubmissionWithActivities) {
        // TODO handle null uid
        if (uid != null) {
            viewModelScope.launch {
                val response = submissionRepository.fetchDailyStatsForSubmission(
                    submissionWithActivities.submission.remoteId,
                    uid
                )
                selectedSubmissionDailyStats.value = response
            }
        }
    }
}
