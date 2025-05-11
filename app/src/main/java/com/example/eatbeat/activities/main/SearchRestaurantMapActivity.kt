package com.example.eatbeat.activities.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.view.View
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

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

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

        /**
         * Adds a snap helper to the carousel to make it scroll horizontally.
         */
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(restaurantCarousel)

        activateNavBar(this, this, 1)

        /**
         * Adds a listener to the scroll action. When the restaurant is scrolled, it will
         * take its position, and update the map location to see the current one.
         */
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

    /**
     * Updates the map location.
     */
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

    /**
     * Gets the latitude and longitude based on the address provided via geocoding.
     */
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
                val pointAnnotationManager: PointAnnotationManager = annotationApi.createPointAnnotationManager()

                val originalBitmap = bitmapFromDrawableRes(context, R.drawable.restaurant_waypoint)

                val scaledBitmap = scaleBitmap(originalBitmap!!, 100, 100)

                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(longitude, latitude))
                    .withIconImage(scaledBitmap)

                pointAnnotationManager.create(pointAnnotationOptions)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Reescales the image to make it fit on screen.
     */
    private fun scaleBitmap(original: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(original, width, height, false)
    }

    /**
     * Gets the pointer from the drawable, and loads it on the map.
     */
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resId: Int): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resId)
    }
}
