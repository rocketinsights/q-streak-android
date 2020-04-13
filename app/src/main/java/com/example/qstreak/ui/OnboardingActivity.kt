package com.example.qstreak.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.qstreak.R
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {

    // TODO handle launching of app when uid is already available

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val onboardingAdapter = OnboardingAdapter(this, 2)
        onboarding_view_pager.adapter = onboardingAdapter
        onboarding_view_pager.isUserInputEnabled = false
        viewPager = onboarding_view_pager
    }

    fun onSetupButtonClicked() {
        viewPager.setCurrentItem(OnboardingSignupFragment.ONBOARDING_ADAPTER_POSITION, true)
    }
}