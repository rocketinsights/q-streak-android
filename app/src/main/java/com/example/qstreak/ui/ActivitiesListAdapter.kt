package com.example.qstreak.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.models.Activity
import com.example.qstreak.utils.ImageUtils
import kotlinx.android.synthetic.main.submission_activity_item.view.*
import kotlinx.android.synthetic.main.submission_activity_item.view.activity_name
import kotlinx.android.synthetic.main.submission_location_list_item.view.*

class ActivitiesListAdapter(activities: List<Activity>) :
    RecyclerView.Adapter<ActivitiesListAdapter.ActivityViewHolder>() {

    private val activities = mutableListOf<Activity>().apply {
        this.addAll(activities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.submission_location_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.activityName.text = activity.name
        holder.activityIcon.setImageResource(ImageUtils.getImageByLocation(activity.activitySlug))
    }

    inner class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityIcon: ImageView = view.activity_icon
        val activityName: TextView = view.activity_name
    }

    internal fun setActivities(activities: List<Activity>) {
        this.activities.clear()
        this.activities.addAll(activities)
        notifyDataSetChanged()
    }
}
