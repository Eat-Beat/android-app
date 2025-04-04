package com.example.eatbeat.activities.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.CarouselAdapter
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import kotlinx.coroutines.launch
import java.util.Locale

class SearchRestaurantMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_restaurant_map)

        val restaurantCarousel = findViewById<RecyclerView>(R.id.restaurantCarousel)


        val mapView = findViewById<MapView>(R.id.mapRestaurant)

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(2.17328, 41.38868))
                .zoom(14.0)
                .build()
                                        )

        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()
                val restaurantsList = restaurants?.toMutableList() as ArrayList<Restaurant>
                restaurantCarousel.adapter = CarouselAdapter(restaurantsList)

            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        restaurantCarousel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(restaurantCarousel)

        activateNavBar(this, this, 1)

        restaurantCarousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(restaurantCarousel.layoutManager) ?: return
                    val position = restaurantCarousel.layoutManager?.getPosition(centerView) ?: return
                    updateMapLocation(this@SearchRestaurantMapActivity, position)
                }
            }
        })

        val searchButton = findViewById<ImageView>(R.id.searchiconmap)
        searchButton.setOnClickListener {
            val intent = Intent(this, ManualSearchRestaurant::class.java)
            startActivity(intent)
            finish()
        }

        val listButton = findViewById<ConstraintLayout>(R.id.seeListRestaurant)
        listButton.setOnClickListener {
            val intent = Intent(this, SearchRestaurantActivity::class.java)
            startActivity(intent)
            finish()
        }

        activateNavBar(this, this, 1)
    }

    private fun updateMapLocation(context: Context, position: Int) {
        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()
                val restaurantsList = restaurants?.toMutableList() as ArrayList<Restaurant>
                if (position in restaurantsList.indices) {
                    val restaurant = restaurantsList[position]
                    val fullAddress = "${restaurant.getAddress()}, ${restaurant.zipCode}"
                    chargeLocation(context, fullAddress)
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun chargeLocation(context: Context, address: String) {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                val latitude = location.latitude
                val longitude = location.longitude

                val mapView = findViewById<MapView>(R.id.mapRestaurant)
                mapView.getMapboxMap().setCamera(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(longitude, latitude))
                        .zoom(14.0)
                        .build()
                )

                val annotationApi = mapView.annotations
                val pointAnnotationManager: PointAnnotationManager =
                    annotationApi.createPointAnnotationManager()


                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(longitude, latitude))
                    .withIconImage(bitmapFromDrawableRes(context, R.drawable.restaurant_waypoint)!!)

                pointAnnotationManager.create(pointAnnotationOptions)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resId: Int): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resId)
    }
}
