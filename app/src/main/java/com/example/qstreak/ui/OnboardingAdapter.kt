package com.example.qstreak.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(activity: AppCompatActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // TODO set values to variables named for fragments
            OnboardingLogoFragment.ONBOARDING_ADAPTER_POSITION -> OnboardingLogoFragment()
            OnboardingSignupFragment.ONBOARDING_ADAPTER_POSITION -> OnboardingSignupFragment()
            else -> OnboardingLogoFragment()
        }
    }
}