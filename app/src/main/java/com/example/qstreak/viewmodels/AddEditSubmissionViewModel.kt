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
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AddEditSubmissionViewModel(
    private val submissionRepository: SubmissionRepository,
    private val activitiesRepository: ActivitiesRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    // TODO is this locale okay to use?
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    val activities: LiveData<List<Activity>> = activitiesRepository.activities
    val checkedActivities = MutableLiveData<List<Activity>>()

    val submissionComplete = MutableLiveData<Boolean>(false)
    val newSubmissionDate = MutableLiveData<Date>(Date())
    val contactCount = MutableLiveData<String>()
    val existingSubmission = MutableLiveData<SubmissionWithActivities>()
    val errorToDisplay = MutableLiveData<String>()

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

    fun getSubmissionByDate(date: String) {
        viewModelScope.launch {
            try {
                val submission = submissionRepository.getSubmissionWithActivitiesByDate(date)
                existingSubmission.value = submission
            } catch (e: Exception) {
                errorToDisplay.value = ("Error loading date: $date")
                Timber.e(e)
            }
        }
    }

    fun saveSubmission() {
        // TODO handle null uid
        if (uid != null && isUserInputValid()) {
            try {
                viewModelScope.launch {
                    val response = existingSubmission.value?.let {
                        updateExistingSubmission(
                            it.submission.remoteId,
                            contactCount.value!!.toInt(),
                            checkedActivities.value.orEmpty(),
                            uid as String
                        )
                    } ?: createNewSubmission(
                        contactCount.value!!.toInt(),
                        dateFormatter.format(newSubmissionDate.value!!),
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
        remoteId: Int,
        contactCount: Int,
        activities: List<Activity>,
        uid: String
    ): ApiResult<SubmissionResponse> {
        return submissionRepository.updateSubmission(remoteId, contactCount, activities, uid)
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
