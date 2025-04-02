package com.example.eatbeat.activities.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.RestaurantAdapter
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.users.User
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import kotlinx.coroutines.launch

class SearchRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_restaurant)
        activateNavBar(this, this, 1)

        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()
                val restaurantsList = restaurants?.toMutableList() as ArrayList<Restaurant>
                loadRestaurants(restaurantsList)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        val mapButton = findViewById<ConstraintLayout>(R.id.mapSeeRestaurant)
        mapButton.setOnClickListener {
            val intent = Intent(this, SearchRestaurantMapActivity::class.java)
            startActivity(intent)
            finish()
        }

        val searchButton = findViewById<ImageView>(R.id.searchicon)
        searchButton.setOnClickListener {
            val intent = Intent(this, ManualSearchRestaurant::class.java)
            startActivity(intent)
            finish()
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