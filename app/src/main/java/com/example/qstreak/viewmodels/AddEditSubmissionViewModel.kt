package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Activity
import com.example.qstreak.models.SubmissionWithActivities
import com.example.qstreak.network.ApiResult
import com.example.qstreak.network.SubmissionResponse
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.math.max

class AddEditSubmissionViewModel(
    private val submissionRepository: SubmissionRepository,
    private val activitiesRepository: ActivitiesRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    // Used in display of Add/Edit Record screen
    val activities: LiveData<List<Activity>> = activitiesRepository.activities
    val checkedActivities = MutableLiveData<List<Activity>>(emptyList())
    val selectedDateDisplayString =
        MutableLiveData(DateUtils.getDateDisplayStringFromDate())
    val contactCount = MutableLiveData<String>()
    val errorToDisplay = MutableLiveData<String>()

    // Observe for when to close add/edit screen
    val submissionComplete = MutableLiveData(false)

    // Observe for updating selection in other screens
    val selectedDateString =
        MutableLiveData(DateUtils.dateStringFormat.format(Calendar.getInstance().time))

    // Observe whether currently selected date has data
    val existingSubmission = MutableLiveData<SubmissionWithActivities?>()

    private val uid: String? by lazy {
        sharedPreferences.getString(UID, null)
    }

    fun refreshActivities() {
        // TODO handle null uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    activitiesRepository.refreshActivities(uid as String)
                } catch (e: Exception) {
                    Timber.e("Error message: %s", e.message)
                }
            }
        }
    }

    fun loadDate(dateString: String) {
        viewModelScope.launch {
            try {
                val submission = submissionRepository.getSubmissionWithActivitiesByDate(dateString)
                existingSubmission.value = submission
                selectedDateString.value = dateString
                selectedDateDisplayString.value =
                    DateUtils.getDateDisplayStringFromDbRecord(dateString)
                if (submission != null) {
                    contactCount.value = submission.submission.contactCount.toString()
                    checkedActivities.value = submission.activities
                } else {
                    contactCount.value = "0"
                    checkedActivities.value = emptyList()
                }
            } catch (e: Exception) {
                errorToDisplay.value = ("Error loading date: $dateString")
                Timber.e(e)
            }
        }
    }

    fun incrementContactCount() {
        val nextContactCount = contactCount.value?.toInt()?.inc() ?: 1
        contactCount.value = nextContactCount.toString()
    }

    fun decrementContactCount() {
        val previousContactCount = max(0, contactCount.value?.toInt()?.dec() ?: 0)
        contactCount.value = previousContactCount.toString()
    }

    fun saveSubmission() {
        // TODO handle null uid
        if (uid != null && isUserInputValid()) {
            try {
                viewModelScope.launch {
                    val response = existingSubmission.value?.let {
                        updateExistingSubmission(
                            selectedDateString.value!!,
                            contactCount.value!!.toInt(),
                            checkedActivities.value.orEmpty(),
                            uid as String
                        )
                    } ?: createNewSubmission(
                        contactCount.value!!.toInt(),
                        selectedDateString.value!!,
                        checkedActivities.value.orEmpty(),
                        uid as String
                    )

                    if (response is ApiResult.Success) {
                        submissionComplete.value = true
                    } else {
                        errorToDisplay.value = (response as ApiResult.Error).apiErrors
                    }
                }
            } catch (e: Exception) {
                errorToDisplay.value = e.message
            }
        }
    }

    private suspend fun createNewSubmission(
        contactCount: Int,
        date: String,
        activities: List<Activity>,
        uid: String
    ): ApiResult<SubmissionResponse> {
        return submissionRepository.createSubmission(contactCount, date, activities, uid)
    }

    private suspend fun updateExistingSubmission(
        date: String,
        contactCount: Int,
        activities: List<Activity>,
        uid: String
    ): ApiResult<SubmissionResponse> {
        return submissionRepository.updateSubmission(date, contactCount, activities, uid)
    }

    private fun isUserInputValid(): Boolean {
        // TODO
        return true
    }

    fun onActivityCheckboxToggled(activity: Activity) {
        val currentActivities = arrayListOf<Activity>()
        currentActivities.addAll(checkedActivities.value.orEmpty())
        if (currentActivities.contains(activity)) {
            currentActivities.remove(activity)
        } else {
            currentActivities.add(activity)
        }
        checkedActivities.value = currentActivities
    }
}
