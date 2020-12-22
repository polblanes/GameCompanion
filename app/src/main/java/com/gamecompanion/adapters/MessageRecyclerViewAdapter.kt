package com.gamecompanion

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRecyclerViewAdapter(var items: List<MessageModel>) : RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemUsername: TextView
        var itemDate: TextView
        var itemBody: TextView

        init {
            itemUsername = itemView.findViewById(R.id.message_username)
            itemDate = itemView.findViewById(R.id.message_date)
            itemBody = itemView.findViewById(R.id.message_body)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_message_card, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = items[position]
        //TODO: set username text
        holder.itemBody.text = item.text
        holder.itemDate.text = item.text
    }

    override fun getItemCount(): Int {
        return items.size
    }
}