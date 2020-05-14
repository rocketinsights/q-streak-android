package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.DashboardRepository
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.network.ApiResult
import com.example.qstreak.network.DashboardMessage
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.utils.ImageUtils
import com.example.qstreak.utils.UID
import com.example.qstreak.utils.USER_NAME
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel(
    private val submissionRepository: SubmissionRepository,
    private val dashboardRepository: DashboardRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {
    val currentScoreImage = MutableLiveData<Int>()
    val hasEntryForToday = MutableLiveData<Boolean>()
    val uid: String? = sharedPrefs.getString(UID, null)
    val dashboardMessages = MutableLiveData<List<DashboardMessage>>()
    val showRiskWarning = MutableLiveData<Boolean>(false)
    val countyName = MutableLiveData<String>()
    val errorToDisplay = MutableLiveData<String>()

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

    fun refreshDashboardMessages() {
        // Clear list to avoid clunky visual change in content.
        dashboardMessages.value = emptyList()
        showRiskWarning.value = false

        if (uid != null) {
            viewModelScope.launch {
                val response = dashboardRepository.getDashboardContent(uid)
                if (response is ApiResult.Success) {
                    dashboardMessages.value = response.data.messages
                    countyName.value = response.data.dailyStats.location?.county ?: "Your area"
                    showRiskWarning.value = true
                } else {
                    errorToDisplay.value = (response as ApiResult.Error).apiErrors
                }
            }
        }
    }
}
