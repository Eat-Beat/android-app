package com.example.eatbeat.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.RestaurantAdapter
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson

class SearchRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_restaurant)

        val restaurants = loadRestaurantsFromJson(loadJsonFromRaw(this, R.raw.restaurans)!!)

        loadRestaurants(restaurants)

        activateNavBar(this, this, 1)

        val mapButton = findViewById<ConstraintLayout>(R.id.mapSeeRestaurant)
        mapButton.setOnClickListener {
            val intent = Intent(this, SearchRestaurantMapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadRestaurants(restaurants: ArrayList<Restaurant>) {

        val restaurantRecycler = findViewById<RecyclerView>(R.id.searchMusicRecyclerView)

        restaurantRecycler.layoutManager = LinearLayoutManager(this)

        restaurantRecycler.adapter = RestaurantAdapter(restaurants){ restaurant -> openProfile(restaurant)}
    }

    private fun openProfile(restaurant: Restaurant){
        val intent = Intent(this, ViewRestaurantActivity::class.java)
        intent.putExtra("restaurantId", restaurant.getId())
        startActivity(intent)
    }
}