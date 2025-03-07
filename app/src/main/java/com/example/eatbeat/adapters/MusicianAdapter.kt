package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician

class MusicianAdapter (private val images: List<Musician>) :
    RecyclerView.Adapter<MusicianAdapter.MusicianViewHolder>() {

    class MusicianViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.musicianImageBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.musician_list_cell, parent, false)
        return MusicianViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicianViewHolder, position: Int) {
        holder.image.setImageResource(images[position])
    }

    override fun getItemCount() = images.size
}