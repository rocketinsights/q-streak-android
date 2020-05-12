package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentProfileBinding
import com.example.qstreak.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by lazy {
        (requireActivity() as MainActivity).profileViewModel
    }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_profile,
            null,
            false
        )
        binding.lifecycleOwner = activity
        binding.viewModel = profileViewModel

//        setupEditClickListener()
        setupBackButtonClickListener()

        return binding.root
    }

//    private fun setupEditClickListener() {
//        binding.editButton.setOnClickListener {
//            (requireActivity() as MainActivity).navigateToEditProfile()
//        }
//    }

    private fun setupBackButtonClickListener() {
        binding.backButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
    }


    companion object {
        const val TAG = "ProfileFragment"

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
