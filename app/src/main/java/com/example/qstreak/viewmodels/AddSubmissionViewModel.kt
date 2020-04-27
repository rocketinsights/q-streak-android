package com.example.qstreak.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Activity
import com.example.qstreak.models.Submission
import com.example.qstreak.utils.UID
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AddSubmissionViewModel(
    private val submissionRepository: SubmissionRepository,
    private val activitiesRepository: ActivitiesRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    // TODO is this locale okay to use?
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    val activities: LiveData<List<Activity>> = activitiesRepository.activities
    private val checkedActivities = arrayListOf<Activity>()

    val submissionComplete = MutableLiveData<Boolean>(false)
    val newSubmissionDate = MutableLiveData<Date>(Date())
    val contactCount = MutableLiveData<String>()

    val uid: String? by lazy {
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

    fun createSubmission() {
        // TODO handle null uid
        if (uid != null && isUserInputValid()) {
            val submission = Submission(
                dateFormatter.format(newSubmissionDate.value!!),
                contactCount.value!!.toInt()
            )
            viewModelScope.launch {
                try {
                    submissionRepository.insert(submission, checkedActivities, uid as String)
                    submissionComplete.value = true
                } catch (e: Exception) {
                    // TODO retrieve error text from response body to surface to user (at api layer)
                    Timber.e("Error message: %s", e.message)
                } finally {
                    checkedActivities.clear()
                }
            }
        }
    }

    private fun isUserInputValid(): Boolean {
        // TODO
        return true
    }

    fun onActivityCheckboxToggled(activity: Activity) {
        if (checkedActivities.contains(activity)) {
            checkedActivities.remove(activity)
        } else {
            checkedActivities.add(activity)
        }
    }
}
