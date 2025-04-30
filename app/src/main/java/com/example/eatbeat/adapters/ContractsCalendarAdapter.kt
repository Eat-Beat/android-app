package com.example.eatbeat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import java.text.SimpleDateFormat
import java.util.Locale

class ContractsCalendarAdapter(
    private val contracts: List<Perform>,
    private val musicians: List<Musician>,
    private val restaurants : List<Restaurant>
    ) : RecyclerView.Adapter<ContractsCalendarAdapter.CalendarListHolder>() {

    class CalendarListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayCell: TextView = view.findViewById(R.id.contractDayView)
        val userCell: TextView = view.findViewById(R.id.userNameView)
        val professionCell: TextView = view.findViewById(R.id.userProfessionView)
        val genreCell: TextView = view.findViewById(R.id.userGenreView)
        val hourCell: TextView = view.findViewById(R.id.contractHourView)
        val profilePictureCell: ImageView = view.findViewById(R.id.userPrfIcon)
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
        val restaurant = restaurants.find { it.getId() == contract.getIdRestaurant() }!!

        val date = contract.getDate()
        val hours = date.hours.toString().padStart(2, '0')
        val minutes = date.minutes.toString().padStart(2, '0')
        val time = "$hours:$minutes"
        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())

        holder.dayCell.text = contract.getDate().let { dateFormat.format(it) }
        holder.userCell.text = musician.getName()
        holder.professionCell.text = musician.getClassification()[0]
        holder.genreCell.text = musician.getGenre()[0]
        holder.hourCell.text = time

        if (UserData.userType == 1){
            holder.userCell.text = restaurant.getName()

            Glide.with(holder.itemView.context)
                .load(restaurant.getMultimedia().getImage())
                .into(holder.profilePictureCell)


        } else if (UserData.userType == 2){
            holder.userCell.text = musician.getName()

            Glide.with(holder.itemView.context)
                .load(musician.getMultimedia()[0].getImage())
                .into(holder.profilePictureCell)

        }

    }

    override fun getItemCount() = contracts.size
}