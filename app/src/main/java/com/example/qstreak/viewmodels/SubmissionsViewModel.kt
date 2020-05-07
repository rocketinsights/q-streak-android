package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.DailyLogItemInfo
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

    val selectedDateString = MutableLiveData<String>()
    val selectedSubmission = MutableLiveData<SubmissionWithActivities>()
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

    fun selectDate(dateString: String = dateStringFormat.format(Calendar.getInstance().time)) {
        selectedDateString.value = dateString
        viewModelScope.launch {
            val submissionForDateOrNull =
                submissionRepository.getSubmissionWithActivitiesByDate(dateString)
            submissionForDateOrNull?.let { loadSubmission(it) }
        }
    }

    fun refreshDate() {
        selectedDateString.value?.let {
            selectDate(it)
        } ?: run {
            selectDate()
        }
    }

    fun loadSubmission(submissionWithActivities: SubmissionWithActivities) {
        selectedSubmission.value = submissionWithActivities
        selectedSubmissionScoreImage.value =
            ImageUtils.getImageByScore(submissionWithActivities.submission.score)
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
}
