package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.chat.ProfileCell

class ChatsAdapter (
    private val profileCells: List<ProfileCell>,
    private val openChat : (ProfileCell) -> Unit): RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    inner class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profileIcon: ImageView = view.findViewById(R.id.profileIconView)
        private val profileName : TextView = view.findViewById(R.id.profileNameCell)

        fun bind(profileCell: ProfileCell){
            Glide.with(itemView.context)
                .load(profileCell.profileImage.getImage())
                .into(profileIcon)

            profileName.text = profileCell.name

            itemView.setOnClickListener {
                openChat(profileCell)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_profile_cell, parent, false)
        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val profileCell = profileCells[position]

        holder.bind(profileCell)
    }

    override fun getItemCount() = profileCells.size
}