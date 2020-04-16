package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentAddSubmissionBinding
import com.example.qstreak.models.Submission
import com.example.qstreak.viewmodels.SubmissionsViewModel

class AddSubmissionFragment : Fragment() {
    lateinit var binding: FragmentAddSubmissionBinding
    private val submissionsViewModel: SubmissionsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_add_submission,
            null,
            false
        )
        binding.submissionsViewModel = this.submissionsViewModel

        binding.submitButton.setOnClickListener {
            // TODO data validation
            val submission = Submission(
                binding.dateTextInputLayout.editText?.text.toString(),
                binding.contactCountTextInputLayout.editText?.text.toString().toInt()
            )
            submissionsViewModel.createSubmission(submission, requireContext())
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    companion object {
        const val TAG = "AddSubmissionFragment"
    }
}