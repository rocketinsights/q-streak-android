package com.example.qstreak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.qstreak.R
import com.example.qstreak.databinding.FragmentEditProfileNameBinding
import com.example.qstreak.viewmodels.EditProfileViewModel
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileNameFragment : Fragment() {

    private val editProfileViewModel: EditProfileViewModel by currentScope.viewModel(this)

    private lateinit var binding: FragmentEditProfileNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_edit_profile_name,
            null,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = editProfileViewModel

        setupBackButtonClickListener()
        observeErrors()
        observeUpdated()

        return binding.root
    }

    private fun observeUpdated() {
        editProfileViewModel.profileUpdated.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }

    private fun observeErrors() {
        editProfileViewModel.errorToDisplay.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }


    private fun setupBackButtonClickListener() {
        binding.backButton.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
    }

    companion object {
        const val TAG = "EditProfileNameFragment"

        fun newInstance(): EditProfileNameFragment {
            return EditProfileNameFragment()
        }
    }
}
