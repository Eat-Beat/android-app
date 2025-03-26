package com.example.eatbeat.activities.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eatbeat.R
import com.example.eatbeat.utils.activateNavBar

class SearchRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_restaurant)


        activateNavBar(this, this, 1)
    }
}