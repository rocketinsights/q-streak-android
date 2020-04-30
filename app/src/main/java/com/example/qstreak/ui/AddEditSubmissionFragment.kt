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
import com.example.qstreak.databinding.FragmentAddEditSubmissionBinding
import com.example.qstreak.models.Activity
import com.example.qstreak.viewmodels.AddEditSubmissionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditSubmissionFragment : Fragment() {

    private val addEditViewModel: AddEditSubmissionViewModel by currentScope.viewModel(this)
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

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
        arguments?.getString(SUBMISSION_ID_KEY)?.let {
            addEditViewModel.getSubmissionByDate(it)
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

    private fun observeExistingSubmission() {
        addEditViewModel.existingSubmission.observe(viewLifecycleOwner, Observer { existing ->
            addEditViewModel.contactCount.value = existing.submission.contactCount.toString()
            addEditViewModel.newSubmissionDate.value = dateFormatter.parse(existing.submission.date)
            addEditViewModel.checkedActivities.value = existing.activities
        })
    }

    private fun observeCompletion() {
        addEditViewModel.submissionComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                onSubmissionCompleted()
            }
        })
    }

    private fun setDateClickListener() {
        binding.dateButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                // Log.d("DatePicker Activity", "Date String = ${picker.headerText}:: Date epoch value = ${it}")
                val pickedDate = Date(it)
                // TODO viewmodel needs to check repository for existing submission w this date
                addEditViewModel.newSubmissionDate.value = pickedDate
            }
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }
    }

    private fun onActivityToggled(activity: Activity) {
        addEditViewModel.onActivityCheckboxToggled(activity)
    }

    companion object {
        const val TAG = "AddSubmissionFragment"
        const val SUBMISSION_ID_KEY = "submission_id"

        fun newInstance(existingSubmissionDate: String?): AddEditSubmissionFragment {
            val fragment = AddEditSubmissionFragment()
            existingSubmissionDate?.let { date ->
                fragment.arguments = Bundle().apply { this.putString(SUBMISSION_ID_KEY, date) }
            }
            return fragment
        }
    }
}
