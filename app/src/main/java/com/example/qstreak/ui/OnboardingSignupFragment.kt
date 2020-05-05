package com.example.qstreak.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentOnboardingSignupBinding
import com.example.qstreak.viewmodels.OnboardingViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingSignupFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by currentScope.viewModel(this)
    private lateinit var binding: FragmentOnboardingSignupBinding

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
        binding.lifecycleOwner = activity
        binding.viewModel = onboardingViewModel

        observeSignup()
        observeErrors()

        return binding.root
    }

    private fun observeSignup() {
        onboardingViewModel.signupSuccessful.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToDashboard()
            }
        })
    }

    private fun observeErrors() {
        onboardingViewModel.errorToDisplay.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        onboardingViewModel.zipCodeError.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.zipCodeInput.error = getString(R.string.zip_invalid)
                if (onboardingViewModel.nameError.value == false) {
                    binding.zipCodeInput.editText?.let { et ->
                        et.requestFocus()
                        et.setSelection(et.text.length)
                    }
                }
            }
        })
        onboardingViewModel.nameError.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.nameInput.error = getString(R.string.name_invalid)
                binding.nameInput.editText?.let { et ->
                    et.requestFocus()
                    et.setSelection(et.text.length)
                }
            }
        })
    }

    private fun navigateToDashboard() {
        startActivity(
            MainActivity.newInstance(
                requireContext(),
                (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        )
    }

    companion object {
        const val ONBOARDING_ADAPTER_POSITION = 1
    }
}
