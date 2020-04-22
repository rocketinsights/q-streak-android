package com.example.qstreak.ui

import android.content.Intent

class OnboardingSubmissionFragment : AddSubmissionFragment() {

    override fun onSubmissionCompleted() {
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val intent = Intent(activity, SubmissionsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        const val ONBOARDING_ADAPTER_POSITION = 2
    }
}
