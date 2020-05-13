package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentEditProfileBinding
import com.example.qstreak.viewmodels.ProfileViewModel

class EditProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by lazy {
        (requireActivity() as MainActivity).profileViewModel
    }
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_edit_profile,
            null,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = profileViewModel

        setupEditNameClickListener()
        setupEditZipCodeClickListener()
        setupBackButtonClickListeners()

        return binding.root
    }

    private fun setupEditNameClickListener() {
        binding.userName.setOnClickListener {
            (requireActivity() as MainActivity).navigateToEditProfileName()
        }
    }

    private fun setupEditZipCodeClickListener() {
        binding.userZip.setOnClickListener {
            (requireActivity() as MainActivity).navigateToEditProfileZipCode()
        }
    }


    private fun setupBackButtonClickListeners() {
        binding.backButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
        binding.doneButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
    }

    companion object {
        const val TAG = "EditProfileFragment"

        fun newInstance(): EditProfileFragment {
            return EditProfileFragment()
        }
    }
}
