package com.example.eatbeat.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician

class MusicianAdapter(
    private val musicians: ArrayList<Musician>,
    private val viewProfile : (Musician) -> Unit
) : RecyclerView.Adapter<MusicianAdapter.MusicianViewHolder>() {

    inner class MusicianViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val musicianImage: ImageView = view.findViewById(R.id.musicianImage)
        private val musicianName : TextView = view.findViewById(R.id.musicianNameBox)
        private val rol : TextView = view.findViewById(R.id.rolBox)
        private val location: TextView = view.findViewById(R.id.locationBox)
        private val rating: TextView = view.findViewById(R.id.ratingNumBox)

        fun bind(musician: Musician) {
            if (musician.getMultimedia().isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(musician.getMultimedia()[0].getImage())
                    .into(musicianImage)
            } else {
                musicianImage.setImageResource(R.drawable.user_selected)
            }

            musicianName.text = musician.getName()
            rol.text = musician.getGenre()[0]
            location.text = musician.calculateLocationName(itemView.context)
            rating.text = musician.getRating().toString()

            itemView.setOnClickListener {
                viewProfile(musician)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.musician_list_cell, parent, false)
        return MusicianViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicianViewHolder, position: Int) {
        val musician = musicians[position]

        holder.bind(musician)
    }

    override fun getItemCount() = musicians.size
}