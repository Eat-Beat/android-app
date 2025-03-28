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

class CarouselAdapter(private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<CarouselAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.restaurantImageBox)
        val restaurantName = view.findViewById<TextView>(R.id.restaurantNameBox)
        val directionBox = view.findViewById<TextView>(R.id.directionBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.map_slider_cell, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]

        Glide.with(holder.itemView.context)
            .load(restaurant.getMultimedia().getImage())
            .into(holder.image)

        holder.restaurantName.text = restaurant.getName()
        holder.directionBox.text = restaurant.getAddress()
    }

    override fun getItemCount() = restaurants.size
}
