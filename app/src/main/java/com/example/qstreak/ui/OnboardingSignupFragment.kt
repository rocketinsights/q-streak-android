package com.example.qstreak.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentOnboardingSignupBinding
import com.example.qstreak.viewmodels.OnboardingViewModel

class OnboardingSignupFragment : Fragment() {

    companion object {
        const val ONBOARDING_ADAPTER_POSITION = 1
    }

    private lateinit var binding: FragmentOnboardingSignupBinding
    private val onboardingViewModel: OnboardingViewModel by lazy {
        ViewModelProvider(this).get(OnboardingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_onboarding_signup,
            null,
            false
        )
        binding.continueButton.setOnClickListener {
            createUser()
        }
        observeSignup()

        return binding.root
    }

    private fun createUser() {
        // TODO data validation - zip required, age and household size optional
        onboardingViewModel.createUser(
            requireContext(),
            binding.ageInput.editText?.text.toString().toInt(),
            binding.householdSizeInput.editText?.text.toString().toInt(),
            binding.zipCodeInput.editText?.text.toString()
        )
    }

    private fun observeSignup() {
        onboardingViewModel.signupSuccessful.observe(viewLifecycleOwner, Observer {
            if (it) {
                onSignupSuccess()
            }
        })
    }

    private fun onSignupSuccess() {
        val intent = Intent(activity, SubmissionsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}