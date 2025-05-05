package com.example.eatbeat.activities.main

import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.eatbeat.R
import com.example.eatbeat.adapters.MultimediaAdapter
import com.example.eatbeat.chat.MainChatActivity
import com.example.eatbeat.fragments.StatsFrag
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import kotlinx.coroutines.launch
import java.util.Locale
import javax.security.auth.callback.Callback

class ViewRestaurantActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_restaurant)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val restaurantId = intent.getIntExtra("restaurantId", -1)

        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()
                val restaurantList = restaurants?.toMutableList() as ArrayList<Restaurant>
                val restaurant = restaurantList.find { it.getId() == restaurantId }

                if (restaurant != null){
                    loadInfo(restaurant)
                    chargeLocation(restaurant.getAddress())
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val backButton = findViewById<ImageView>(R.id.backButton)

        /**
         * Initiates a chat with the user.
         */
        val messageButton = findViewById<ImageView>(R.id.chatIcon)
        messageButton.setOnClickListener {
            val intent = Intent(this, MainChatActivity::class.java)
            intent.putExtra("userId", restaurantId)
            startActivity(intent)
        }

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 530
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        backButton.setOnClickListener {
            finish()
        }

    }

    /**
     * Charges the location onto the map based on the address provided via geocoding.
     */
    private fun chargeLocation(address: String) {
        val geocoder = Geocoder(this, Locale.getDefault())

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

    /**
     * Loads the restaurant's info. If no images have been provided, sets a default
     * image.
     */
    private fun loadInfo(restaurant: Restaurant){
        val profileUserName = findViewById<TextView>(R.id.profileUserName)
        val description = findViewById<TextView>(R.id.profileDescription)

        profileUserName.text = restaurant.getName()

        description.text = restaurant.getAddress() + " " + restaurant.getAddressNum()

        val imageUrl = restaurant.getMultimedia().getImage()

        val multimedia = restaurant.getMultimedia()
        if (multimedia == null || multimedia.getImage() == null) {
            val profileImage = findViewById<ImageView>(R.id.pflUserLargeImage)
            Glide.with(this)
                .load(R.drawable.not_load_restaurant_bc)
                .into(profileImage)
        } else {
            val profileImage = findViewById<ImageView>(R.id.pflUserLargeImage)
            Glide.with(this)
                .load(imageUrl)
                .into(profileImage)
        }

    }
}