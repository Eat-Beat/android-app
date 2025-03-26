package com.example.eatbeat.activities.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.CarouselAdapter
import com.example.eatbeat.utils.activateNavBar
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

class SearchRestaurantMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_restaurant_map)

        val restaurantCarousel = findViewById<RecyclerView>(R.id.restaurantCarousel)

        val images = listOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
                           )

        val mapView = findViewById<MapView>(R.id.mapRestaurant)

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(2.17328, 41.38868))
                .zoom(14.0)
                .build()
                                        )

        restaurantCarousel.adapter = CarouselAdapter(images)
        restaurantCarousel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(restaurantCarousel)

        activateNavBar(this, this, 1)
    }
}