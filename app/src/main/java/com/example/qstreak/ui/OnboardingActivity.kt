package com.example.qstreak.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.qstreak.R
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val onboardingAdapter = OnboardingAdapter(this, 2)
        onboarding_view_pager.adapter = onboardingAdapter
        onboarding_view_pager.isUserInputEnabled = false
        viewPager = onboarding_view_pager
    }

    fun setCurrentItem(adapterPosition: Int) {
        viewPager.setCurrentItem(adapterPosition, true)
    }
}
