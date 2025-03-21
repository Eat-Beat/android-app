package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician

class MultimediaAdapter (private val musician: Musician) :
    RecyclerView.Adapter<MultimediaAdapter.MultimediaViewHolder>() {

    class MultimediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicianImage: ImageView = view.findViewById(R.id.musicianImage)
        val musicianName : TextView = view.findViewById(R.id.musicianNameBox)
        val rol : TextView = view.findViewById(R.id.rolBox)
        val location: TextView = view.findViewById(R.id.locationBox)
        val rating: TextView = view.findViewById(R.id.ratingNumBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultimediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multimedia_cell, parent, false)
        return MultimediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultimediaViewHolder, position: Int) {

        for (i in 0..musician.getMultimedia().size){
            Glide.with(holder.itemView.context)
                .load(musician.getMultimedia()[i].getImage())
                .into(holder.musicianImage)
        }
    }

    override fun getItemCount() = musician.getMultimedia().size
}