package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.utils.USER_NAME
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DashboardViewModel(
    private val submissionRepository: SubmissionRepository,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val userName = MutableLiveData<String>(sharedPrefs.getString(USER_NAME, null))

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
