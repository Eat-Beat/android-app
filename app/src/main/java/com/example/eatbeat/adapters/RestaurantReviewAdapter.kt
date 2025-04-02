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
import com.example.eatbeat.contracts.PerformProfile
import com.example.eatbeat.users.Restaurant

class RestaurantReviewAdapter(private val contracts: ArrayList<PerformProfile>, private val restaurants: ArrayList<Restaurant>) :
    RecyclerView.Adapter<RestaurantReviewAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.profileNameCell)
        val starsReview : RatingBar = view.findViewById(R.id.ratingBar)
        val restaurantProfile : ImageView = view.findViewById(R.id.profileIconView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_review_cell, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val contract = contracts[position]
        val restaurant = restaurants.find { it.getName() == contract.getName() }

        holder.restaurantName.text = contract.getName()
        holder.starsReview.rating = contract.getRate().toFloat()

        Glide.with(holder.itemView.context)
            .load(restaurant?.getMultimedia()?.getImage())
            .into(holder.restaurantProfile)
    }


    override fun getItemCount() = contracts.size
}