package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentAddEditSubmissionBinding
import com.example.qstreak.models.Activity
import com.example.qstreak.utils.DateUtils
import com.example.qstreak.viewmodels.AddEditSubmissionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditSubmissionFragment : Fragment() {

    private val addEditViewModel: AddEditSubmissionViewModel by currentScope.viewModel(this)

    private lateinit var binding: FragmentAddEditSubmissionBinding

    override fun onResume() {
        super.onResume()
        addEditViewModel.refreshActivities()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getString(SUBMISSION_DATE_KEY)?.let {
            addEditViewModel.initializeWithDate(it)
        }

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_add_edit_submission,
            null,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = addEditViewModel

        setupActivitiesList()
        observeExistingSubmission()
        observeCompletion()
        observeErrors()
        setDateClickListener()

        return binding.root
    }

    private fun onSubmissionCompleted() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun setupActivitiesList() {
        val adapter = ActivitiesChecklistAdapter(
            this::onActivityToggled,
            addEditViewModel.activities.value.orEmpty()
        )

        // Set list of possible activities to select from
        addEditViewModel.activities.observe(viewLifecycleOwner, Observer {
            adapter.setActivities(it.orEmpty())
        })

        // Update UI with activities currently checked
        addEditViewModel.checkedActivities.observe(viewLifecycleOwner, Observer {
            adapter.setCheckedActivities(it.orEmpty())
        })

        binding.activitiesChecklist.adapter = adapter
        binding.activitiesChecklist.layoutManager = LinearLayoutManager(activity)
    }

    // TODO this is MVP. How to avoid coming back up to the Fragment here?
    private fun observeExistingSubmission() {
        addEditViewModel.existingSubmission.observe(viewLifecycleOwner, Observer { existing ->
            if (existing != null) {
                // TODO less gross way to do this: (ensures if we return to detail page the correct record is selected)
                (requireActivity() as MainActivity).submissionsViewModel.selectSubmission(existing)

                addEditViewModel.contactCount.value = existing.submission.contactCount.toString()
                addEditViewModel.submissionDate.value =
                    DateUtils.getDateFromDbRecord(existing.submission.date)
                addEditViewModel.submissionDateString.value =
                    DateUtils.getDateStringForAddEditFromDbRecord(existing.submission.date)
                addEditViewModel.checkedActivities.value = existing.activities
            } else {
                addEditViewModel.contactCount.value = null
                addEditViewModel.checkedActivities.value = null
            }
        })
    }

    private fun observeCompletion() {
        addEditViewModel.submissionComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                onSubmissionCompleted()
            }
        })
    }

    private fun observeErrors() {
        addEditViewModel.errorToDisplay.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setDateClickListener() {
        binding.dateButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance().apply {
                    this.time = Date(it)
                    // TODO Don't know why we have to add one to the result of the picker
                    this.add(Calendar.DATE, 1)
                }
                val pickedDate = DateUtils.dateStringFormat.format(calendar.time)
                addEditViewModel.initializeWithDate(pickedDate)
            }
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }
    }

    private fun onActivityToggled(activity: Activity) {
        addEditViewModel.onActivityCheckboxToggled(activity)
    }

    companion object {
        const val TAG = "AddSubmissionFragment"
        const val SUBMISSION_DATE_KEY = "submission_id"

        fun newInstance(existingSubmissionDate: String?): AddEditSubmissionFragment {
            val fragment = AddEditSubmissionFragment()
            existingSubmissionDate?.let { date ->
                fragment.arguments = Bundle().apply { this.putString(SUBMISSION_DATE_KEY, date) }
            }
            return fragment
        }
    }
}
