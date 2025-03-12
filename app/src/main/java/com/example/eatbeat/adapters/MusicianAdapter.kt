package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician

class MusicianAdapter(private val musicians: List<Musician>) :
    RecyclerView.Adapter<MusicianAdapter.MusicianViewHolder>() {

    class MusicianViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.musicianImageBox)
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
        holder.image.setImageResource(musicians[position].getMultimedia()[1].getImage().format)
        holder.musicianName.text = musicians[position].getName()
        holder.rol.text = musicians[position].getGenre()[1].getName()
        holder.location.text = musicians[position].calculateLocationName()
        holder.rating.text = String.format(musicians[position].calculateRating().toString())
    }

    override fun getItemCount() = musicians.size
}