package com.example.eatbeat.adapters

import android.annotation.SuppressLint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician

class ContractsListAdapter(private val contracts: List<Perform>, private val musicians: List<Musician>) :
    RecyclerView.Adapter<ContractsListAdapter.CalendarListHolder>() {

    class CalendarListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayCell: TextView = view.findViewById(R.id.contractDayView)
        val userCell: TextView = view.findViewById(R.id.userNameView)
        val professionCell: TextView = view.findViewById(R.id.userProfessionView)
        val genreCell : TextView = view.findViewById(R.id.userGenreView)
        val hourCell : TextView = view.findViewById(R.id.contractHourView)
        val profilePictureCell : ImageView = view.findViewById(R.id.userPrfIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contract_format_cell, parent, false)
        return CalendarListHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CalendarListHolder, position: Int) {
        val contract = contracts[position]
        val musician = musicians.find { it.getId() == contract.getIdMusician() }!!

        val date = contract.getDate()
        val hours = date.hours.toString().padStart(2, '0')
        val minutes = date.minutes.toString().padStart(2, '0')
        val time = "$hours:$minutes"

        holder.dayCell.text = contract.getDate().toString()
        holder.userCell.text = musician.getName()
        holder.professionCell.text = musician.getClassification()[0]
        holder.genreCell.text = musician.getGenre()[0]
        holder.hourCell.text = time

        Glide.with(holder.itemView.context)
            .load(musician.getMultimedia()[0].getImage())
            .into(holder.profilePictureCell)    }

    override fun getItemCount() = contracts.size
}