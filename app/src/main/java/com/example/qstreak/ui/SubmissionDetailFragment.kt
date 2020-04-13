package com.example.qstreak.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

class SubmissionDetailFragment : Fragment() {

    companion object {

        fun newInstance(submissionId: Int): SubmissionDetailFragment {
            val args = Bundle()
            args.putInt("submissionId", submissionId)
            return SubmissionDetailFragment().apply { this.arguments = args }
        }
    }

}