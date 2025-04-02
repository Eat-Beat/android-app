package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant

class RestaurantAdapter(
    private val restaurants: List<Restaurant>,
    private val viewProfile : (Restaurant) -> Unit) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val restaurantPic: ImageView = view.findViewById(R.id.restaurantProfilePic)
        private val restaurantName : TextView = view.findViewById(R.id.restuarantNameBoxList)
        private val restaurantLocation : TextView = view.findViewById(R.id.restaurantLocationBoxList)


        fun bind(restaurant: Restaurant) {
            Glide.with(itemView.context)
                .load(restaurant.getMultimedia().getImage())
                .into(restaurantPic)

            restaurantName.text = restaurant.getName()
            restaurantLocation.text = restaurant.getAddress()

            itemView.setOnClickListener {
                viewProfile(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_cell, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]

        holder.bind(restaurant)
    }

    override fun getItemCount() = restaurants.size
}