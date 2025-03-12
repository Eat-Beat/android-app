package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician

class MusicianAdapter(private val musicians: List<Musician>) :
    RecyclerView.Adapter<MusicianAdapter.MusicianViewHolder>() {

    class MusicianViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicianImage: ImageView = view.findViewById(R.id.musicianImage)
        val musicianName : TextView = view.findViewById(R.id.musicianNameBox)
        val rol : TextView = view.findViewById(R.id.rolBox)
        val location: TextView = view.findViewById(R.id.locationBox)
        val rating: TextView = view.findViewById(R.id.ratingNumBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.musician_list_cell, parent, false)
        return MusicianViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicianViewHolder, position: Int) {
        val musician = musicians[position]

        Glide.with(holder.itemView.context)
            .load(musician.getMultimedia()[0].getImage())
            .into(holder.musicianImage)

        holder.musicianName.text = musician.getName()
        holder.rol.text = musician.getGenre()[0]
        holder.location.text = musician.calculateLocationName()
        holder.rating.text = String.format(musician.calculateRating().toString())
    }

    override fun getItemCount() = musicians.size
}