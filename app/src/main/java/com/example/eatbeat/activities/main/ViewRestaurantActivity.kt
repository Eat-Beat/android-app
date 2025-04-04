package com.example.eatbeat.activities.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.eatbeat.R
import com.example.eatbeat.adapters.MultimediaAdapter
import com.example.eatbeat.fragments.StatsFrag
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import java.util.Locale
import javax.security.auth.callback.Callback

class ViewRestaurantActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_restaurant)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val restaurantId = intent.getIntExtra("restaurantId", -1)

        val restaurants = loadRestaurantsFromJson(loadJsonFromRaw(this, R.raw.restaurans)!!)
        val restaurant = restaurants.find { it.getId() == restaurantId }

        if (restaurant != null){
            loadInfo(restaurant)
            chargeLocation(this, restaurant.getAddress())
        }

        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val backButton = findViewById<ImageView>(R.id.backButton)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 530
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        backButton.setOnClickListener {
            finish()
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

                val map = findViewById<MapView>(R.id.mapRestaurantProfile)

                map.getMapboxMap().setCamera(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(longitude, latitude))
                        .zoom(14.0)
                        .build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun loadInfo(restaurant: Restaurant){
        val profileUserName = findViewById<TextView>(R.id.profileUserName)

        profileUserName.text = restaurant.getName()

        val imageUrl = restaurant.getMultimedia().getImage()

        Glide.with(this)
            .load(imageUrl)
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable, transition: Transition<in Drawable?>?) {
                    val backgroundPfp = findViewById<CoordinatorLayout>(R.id.coordinatorLayoutRest)
                    backgroundPfp.background = resource
                }
            })
    }
}