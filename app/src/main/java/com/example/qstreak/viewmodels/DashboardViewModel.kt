package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.utils.ImageUtils
import com.example.qstreak.utils.USER_NAME
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel(private val submissionRepository: SubmissionRepository,
                         sharedPrefs: SharedPreferences) : ViewModel() {
    val currentScoreImage = MutableLiveData<Int>()
    val hasEntryForToday = MutableLiveData<Boolean>()
    val userName = MutableLiveData<String>(sharedPrefs.getString(USER_NAME, null))

    fun refreshToday() {
        viewModelScope.launch {
            val submission = submissionRepository.getSubmissionWithActivitiesByDate(
                DateUtils.dateStringFormat.format(Calendar.getInstance().time)
            )
            hasEntryForToday.value = submission != null
            currentScoreImage.value =
                ImageUtils.getDashboardImageByScore(submission?.submission?.score)
        }
    }
}
