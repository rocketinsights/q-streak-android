package com.example.qstreak.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qstreak.db.QstreakDatabase
import com.example.qstreak.db.SubmissionRepository
import com.example.qstreak.models.Submission
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil
import kotlinx.coroutines.launch

class SubmissionsViewModel(application: Application) : AndroidViewModel(application) {
    var submissions: LiveData<List<Submission>> = MutableLiveData()
    // TODO Dependency injection
    private val submissionRepository =
        SubmissionRepository(QstreakDatabase.getInstance(application).submissionDao())

    init {
        submissions = submissionRepository.submissions
    }

    fun createSubmission(submission: Submission, context: Context) {
        // get uid to pass as bearer token
        val uid = EncryptedSharedPreferencesUtil.getUid(context)

        if (uid != null) {
            viewModelScope.launch {
                try {
                    submissionRepository.insert(submission, uid)
                } catch (e: Exception) {
                    // TODO retrieve error text from response body to surface to user (at api layer)
                    Log.e("Submission Insert Error", "Error message: " + e.message)
                }
            }
        } else {
            // TODO handle error case if uid is null
        }
    }

    companion object {
        private const val UID_KEY = "uid"
    }
}