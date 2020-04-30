package com.example.qstreak.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.models.DailyLogItemInfo
import kotlinx.android.synthetic.main.daily_log_item.view.*

class DailyLogAdapter(
    private val onItemClicked: (DailyLogItemInfo) -> Unit
) :
    RecyclerView.Adapter<DailyLogAdapter.DailyLogViewHolder>() {

    private val infos = arrayListOf<DailyLogItemInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyLogViewHolder {
        val holder = DailyLogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.daily_log_item,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            onItemClicked(infos[holder.adapterPosition])
        }

        return holder
    }

    override fun getItemCount(): Int {
        return infos.size
    }

    override fun onBindViewHolder(holder: DailyLogViewHolder, position: Int) {
        val itemInfo = infos[position]
        holder.dayOfWeek.text = itemInfo.dayOfWeek
        holder.dayOfMonth.text = itemInfo.dayOfMonth
        holder.checkImage.setImageResource(getResourceForCircle(itemInfo))
    }

    private fun getResourceForCircle(itemInfo: DailyLogItemInfo): Int {
        return when {
            itemInfo.isComplete -> {
                R.drawable.ic_green_check_filled
            }
            itemInfo.isToday -> {
                R.drawable.ic_gray_check_dotted
            }
            else -> {
                R.drawable.ic_gray_empty_circle
            }
        }
    }

    inner class DailyLogViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val dayOfWeek: TextView = view.day_of_week
        val dayOfMonth: TextView = view.day_of_month
        val checkImage: ImageView = view.check_image
    }

    internal fun setItemInfos(infos: List<DailyLogItemInfo>) {
        this.infos.clear()
        this.infos.addAll(infos)
        notifyDataSetChanged()
    }
}
