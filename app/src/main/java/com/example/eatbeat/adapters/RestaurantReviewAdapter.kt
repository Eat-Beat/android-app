package com.example.eatbeat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import java.util.ArrayList

class RestaurantReviewAdapter(private val contracts : ArrayList<Perform>, private val restaurants : ArrayList<Restaurant>) :
    RecyclerView.Adapter<RestaurantReviewAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.restaurantNameCell)
        val starsReview : RatingBar = view.findViewById(R.id.ratingBar)
        val restaurantProfile : ImageView = view.findViewById(R.id.restaurantIconView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_review_cell, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val contract = contracts[position]
        val restaurant = restaurants.find { it.getId() == contract.getIdRestaurant()  }!!

        holder.restaurantName.text = restaurant.getName()
        holder.starsReview.rating = contract.getRatingRestaurant().toFloat()

        Glide.with(holder.itemView.context)
            .load(restaurant.getMultimedia()[0].getImage())
            .into(holder.restaurantProfile)
    }

    override fun getItemCount() = contracts.size
}