package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.DailyLogItemInfo
import com.example.qstreak.models.DailyStats
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.utils.DateUtils.dateStringFormat
import com.example.qstreak.utils.ImageUtils
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch
import java.util.*

class SubmissionsViewModel(
    private val submissionRepository: SubmissionRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val dailyLogInfos = MutableLiveData<List<DailyLogItemInfo>>()
    val currentWeekString = MutableLiveData<String>().apply {
        this.value = DateUtils.getWeekOfDateString(Calendar.getInstance().time)
    }

    val selectedSubmission = MutableLiveData<SubmissionWithActivities>()
    val selectedSubmissionDailyStats = MutableLiveData<DailyStats>()
    val submissionDeleted = MutableLiveData<Boolean>(false)

    val selectedSubmissionScoreImage = MutableLiveData<Int>()

    private val uid: String? = sharedPrefs.getString(UID, null)

    fun generateDailyLogInfos() {
        val dates = DateUtils.listOfThisAndLastWeekDates()

        viewModelScope.launch {
            val latestInfos = dates.map {
                val submission = submissionRepository.getSubmissionWithActivitiesByDate(
                    dateStringFormat.format(it)
                )
                DailyLogItemInfo(it, submission)
            }

            val sortedInfos = latestInfos.sortedBy { it.date }

            dailyLogInfos.value = sortedInfos
        }
    }

    fun setCurrentWeekBasedOnScrollPosition(position: Int) {
        val firstVisibleDate = dailyLogInfos.value?.get(position)
        firstVisibleDate?.let {
            currentWeekString.value = DateUtils.getWeekOfDateString(it.date)
        }
    }

    fun selectSubmission(submissionWithActivities: SubmissionWithActivities) {
        selectedSubmission.value = submissionWithActivities
        selectedSubmissionDailyStats.value = null
        selectedSubmissionScoreImage.value =
            ImageUtils.getImageByScore(submissionWithActivities.submission.score)
    }

    fun refreshSelectedSubmission() {
        val selectedDate = selectedSubmission.value?.submission?.date
        selectedDate?.let {
            viewModelScope.launch {
                val selectedSubmission =
                    submissionRepository.getSubmissionWithActivitiesByDate(selectedDate)
                selectedSubmission?.let {
                    selectSubmission(it)
                }
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
                    submissionWithActivities.submission.date,
                    uid
                )
                selectedSubmissionDailyStats.value = response
            }
        }
    }
}
