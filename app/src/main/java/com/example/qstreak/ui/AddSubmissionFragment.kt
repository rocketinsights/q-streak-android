package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentAddSubmissionBinding
import com.example.qstreak.models.Activity
import com.example.qstreak.models.Submission
import com.example.qstreak.viewmodels.SubmissionsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class AddSubmissionFragment : Fragment() {
    private val submissionsViewModel: SubmissionsViewModel by lazy {
        (requireActivity() as SubmissionsActivity).sharedViewModel
    }
    private lateinit var binding: FragmentAddSubmissionBinding

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
        binding.lifecycleOwner = activity
        binding.submissionsViewModel = this.submissionsViewModel

        setupActivitiesList()
        setSaveClickListener()
        setDateClickListener()

        return binding.root
    }

    private fun setupActivitiesList() {
        val adapter = ActivitiesChecklistAdapter(
            this::onActivityToggled,
            submissionsViewModel.activities.value.orEmpty()
        )

        submissionsViewModel.activities.observe(viewLifecycleOwner, Observer {
            adapter.setActivities(it)
        })

        binding.activitiesChecklist.adapter = adapter
        binding.activitiesChecklist.layoutManager = LinearLayoutManager(activity)
    }

    private fun setSaveClickListener() {
        binding.saveButton.setOnClickListener {
            // TODO data validation
            val submission = Submission(
                // binding.dateTextInputLayout.editText?.text.toString(),
                "NOTADATE",
                binding.contactCountTextInputLayout.editText?.text.toString().toInt()
            )
            submissionsViewModel.createSubmission(submission)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setDateClickListener() {
        binding.dateButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                // Log.d("DatePicker Activity", "Date String = ${picker.headerText}:: Date epoch value = ${it}")
                val pickedDate = Date(it)
                submissionsViewModel.newSubmissionDate.value = pickedDate
            }
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }
    }

    private fun onActivityToggled(activity: Activity) {
        submissionsViewModel.onActivityCheckboxToggled(activity)
    }

    companion object {
        const val TAG = "AddSubmissionFragment"
    }
}
