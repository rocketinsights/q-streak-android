package com.example.qstreak.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.models.Submission
import kotlinx.android.synthetic.main.submission_item.view.*

class SubmissionsAdapter(private val onItemClicked: (Submission) -> Unit) : RecyclerView.Adapter<SubmissionsAdapter.SubmissionViewHolder>() {
    val submissions = mutableListOf<Submission>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {
        val holder = SubmissionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.submission_item, parent, false))

        holder.itemView.setOnClickListener {
            onItemClicked(submissions[holder.adapterPosition])
        }

        return holder
    }

    override fun getItemCount(): Int {
        return submissions.size
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val submission = submissions[position]
        holder.date.text = submission.date
        holder.contactCount.text = submission.contactCount.toString()
    }

    inner class SubmissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.submission_date
        val contactCount: TextView = view.submission_contact_count
    }

    internal fun setSubmissions(submissions: List<Submission>) {
        this.submissions.clear()
        this.submissions.addAll(submissions)
        notifyDataSetChanged()
    }
}