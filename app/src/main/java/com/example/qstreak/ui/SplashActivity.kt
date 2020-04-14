package com.example.qstreak.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qstreak.R
import com.example.qstreak.utils.EncryptedSharedPreferencesUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        if (isUserRegistered()) {
            navigateToSubmissions()
        } else {
            navigateToOnboarding()
        }
    }

    private fun isUserRegistered(): Boolean {
        return EncryptedSharedPreferencesUtil.getUid(applicationContext) != null
    }

    private fun navigateToOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun navigateToSubmissions() {
        val intent = Intent(this, SubmissionsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}