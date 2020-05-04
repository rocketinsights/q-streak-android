package com.example.qstreak.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.utils.DateUtils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DashboardViewModel(private val submissionRepository: SubmissionRepository) : ViewModel() {
    val testString = MutableLiveData("Data from dashboard viewmodel")

    fun refreshToday() {
        // TODO use this call as needed -- retrieves submission for today if one is available
        viewModelScope.launch {
            val submission = submissionRepository.getSubmissionWithActivitiesByDate(
                DateUtils.dateStringFormat.format(Calendar.getInstance().time)
            )
            Timber.d("submission or null: %s", submission)
        }
    }
}
