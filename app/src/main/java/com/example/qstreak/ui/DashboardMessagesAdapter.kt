package com.example.qstreak.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qstreak.R
import com.example.qstreak.network.DashboardMessage
import kotlinx.android.synthetic.main.dashboard_message_item.view.*

class DashboardMessagesAdapter(private val onUrlClicked: (String?) -> Unit) :
    RecyclerView.Adapter<DashboardMessagesAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<DashboardMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dashboard_message_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageIcon.text = message.icon.toInt(16).toChar().toString()
        holder.messageTitle.text = message.dashboardMessageTitle
        holder.messageBody.text = message.dashboardMessageBody

        if (message.dashboardMessageUrl != null) {
            holder.itemView.setOnClickListener {
                onUrlClicked(message.dashboardMessageUrl)
            }
        }
    }

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageIcon: TextView = view.message_icon
        val messageTitle: TextView = view.message_title
        val messageBody: TextView = view.message_body
    }

    internal fun setMessages(messages: List<DashboardMessage>) {
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }
}
