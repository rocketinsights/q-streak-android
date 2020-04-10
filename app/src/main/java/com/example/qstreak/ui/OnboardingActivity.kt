package com.example.qstreak.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qstreak.R
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {

    private lateinit var onboardingTitlesArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        onboardingTitlesArray = resources.getStringArray(R.array.onboarding_titles)

        val onboardingAdapter = OnboardingAdapter(this, onboardingTitlesArray.size)
        onboarding_view_pager.adapter = onboardingAdapter
    }
}