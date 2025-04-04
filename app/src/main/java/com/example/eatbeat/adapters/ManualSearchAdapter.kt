package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.users.Restaurant

class ManualSearchAdapter (private val restaurants: List<Restaurant>, private val viewProfile : (Restaurant) -> Unit) :
    RecyclerView.Adapter<ManualSearchAdapter.ManualSearchHolder>() {

    class ManualSearchHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.ms_restaurantimage)!!
        val restaurantName = view.findViewById<TextView>(R.id.ms_restaurantname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManualSearchHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.manual_search_restaurant_cell, parent, false)
        return ManualSearchHolder(view)
    }

    override fun onBindViewHolder(holder: ManualSearchHolder, position: Int) {
        val restaurant = restaurants[position]

        holder.restaurantName.text = restaurant.getName()

        val multimedia = restaurant.getMultimedia()
        if (multimedia == null || multimedia.getImage() == null) {
            Glide.with(holder.itemView.context)
                .load(R.drawable.not_load_restaurant_bc)
                .into(holder.image)
        } else {
            Glide.with(holder.itemView.context)
                .load(multimedia.getImage())
                .into(holder.image)
        }

        holder.itemView.setOnClickListener {
            viewProfile(restaurant)
        }
    }

    override fun getItemCount() = restaurants.size
}
