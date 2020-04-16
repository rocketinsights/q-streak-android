package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentSubmissionDetailBinding
import com.example.qstreak.viewmodels.SubmissionsViewModel

class SubmissionDetailFragment : Fragment() {

    private val submissionsViewModel: SubmissionsViewModel by activityViewModels()
    private lateinit var binding: FragmentSubmissionDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_submission_detail,
            null,
            false
        )
        binding.lifecycleOwner = activity
        binding.viewModel = submissionsViewModel
        return binding.root
    }

    companion object {
        const val TAG = "SubmissionDetailFragment"
    }
}
