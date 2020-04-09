package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentNewSubmissionBinding
import com.example.qstreak.models.Submission
import com.example.qstreak.viewmodels.NewSubmissionViewModel

class NewSubmissionFragment(private val onSubmissionReceived: (Submission) -> Unit) :
    DialogFragment() {
    lateinit var binding: FragmentNewSubmissionBinding
    private val newSubmissionViewModel: NewSubmissionViewModel by lazy {
        ViewModelProvider(this).get(NewSubmissionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_new_submission,
            null,
            false
        )
        binding.newSubmissionViewModel = this.newSubmissionViewModel

        binding.submitButton.setOnClickListener {
            // TODO data validation
            val submission = Submission(
                binding.dateTextInputLayout.editText?.text.toString(),
                binding.contactCountTextInputLayout.editText?.text.toString().toInt()
            )
            onSubmissionReceived(submission)
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.NewSubmissionDialogTheme
    }
}