package com.example.qstreak.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.ActivitiesRepository
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Activity
import com.example.qstreak.models.Submission
import com.example.qstreak.models.SubmissionActivitiesPair
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch

class SubmissionsViewModel(application: Application) : AndroidViewModel(application) {
    // TODO Dependency injection
    private val submissionRepository =
        SubmissionRepository(
            QstreakDatabase.getInstance(application).submissionDao(),
            QstreakDatabase.getInstance(application).submissionWithActivityDao()
        )
    private val activitiesRepository =
        ActivitiesRepository(QstreakDatabase.getInstance(application).activitiesDao())

    val submissions: LiveData<List<SubmissionActivitiesPair>> = submissionRepository.submissionsWithActivities
    val activities: LiveData<List<Activity>> = activitiesRepository.activities
    val selectedSubmission = MutableLiveData<SubmissionActivitiesPair>()
    val selectedActivities = arrayListOf<Activity>()

    init {
        refreshActivities(application)
    }

    fun select(submissionActivitiesPair: SubmissionActivitiesPair) {
        selectedSubmission.value = submissionActivitiesPair
    }

    fun createSubmission(
        submission: Submission,
        context: Context
    ) {
        // get uid to pass as bearer token
        val uid = EncryptedSharedPreferencesUtil.getUid(context)

        if (uid != null) {
            viewModelScope.launch {
                try {
                    submissionRepository.insert(submission, selectedActivities, uid)
                } catch (e: Exception) {
                    // TODO retrieve error text from response body to surface to user (at api layer)
                    Log.e("Submission Insert Error", "Error message: " + e.message)
                } finally {
                    selectedActivities.clear()
                }
            }
        } else {
            // TODO handle error case if uid is null
        }
    }

    private fun refreshActivities(context: Context) {
        val uid = EncryptedSharedPreferencesUtil.getUid(context)

        if (uid != null) {
            viewModelScope.launch {
                try {
                    activitiesRepository.refreshActivities(uid)
                } catch (e: Exception) {
                    Log.e("Fetch Activities Error", "Error message: " + e.message)
                }
            }
        }
    }

    fun onActivityToggled(activity: Activity) {
        if (selectedActivities.contains(activity)) {
            selectedActivities.remove(activity)
        } else {
            selectedActivities.add(activity)
        }
    }

    companion object {
        private const val UID_KEY = "uid"
    }
}