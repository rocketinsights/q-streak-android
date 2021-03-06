package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qstreak.R
import kotlinx.android.synthetic.main.fragment_onboarding_logo.view.*

class OnboardingLogoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_logo, container, false)

        view.setup_button.setOnClickListener {
            navigateToSignup()
        }

        return view
    }

    private fun navigateToSignup() {
        (activity as OnboardingActivity).setCurrentItem(OnboardingSignupFragment.ONBOARDING_ADAPTER_POSITION)
    }

    companion object {
        const val ONBOARDING_ADAPTER_POSITION = 0
    }
}
