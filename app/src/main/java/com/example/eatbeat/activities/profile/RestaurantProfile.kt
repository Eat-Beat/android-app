package com.example.eatbeat.activities.profile

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.eatbeat.R
import com.example.eatbeat.data.UserData
import com.example.eatbeat.fragments.EditUserFrag
import com.example.eatbeat.fragments.SettingsFrag
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getMusicianById
import com.example.eatbeat.utils.api.ApiRepository.getRestaurantById
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import kotlinx.coroutines.launch
import java.util.Locale

class RestaurantProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_restaurant)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val optionsButton = findViewById<ImageView>(R.id.settings_icon)
        val editButton = findViewById<ImageView>(R.id.editUserButton)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 530
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()!!
                val restaurant = restaurants.find { UserData.userId == it.getId()}!!
                chargeDetails(restaurant)
                val fullAddress = "${restaurant.getAddress()}, ${restaurant.zipCode}"
                chargeMap(fullAddress)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)
        navProfile.setImageResource(R.drawable.user_selected)

        settingsClick(optionsButton)
        editProfileClick(editButton)

        activateNavBar(this, this, 4)
    }

        private fun chargeMap(address: String) {
            val geocoder = Geocoder(this, Locale.getDefault())

            try {
                val addresses = geocoder.getFromLocationName(address, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val location = addresses[0]
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val map = findViewById<MapView>(R.id.mapRestaurant)

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

    @SuppressLint("SetTextI18n")
    private fun chargeDetails(restaurant: Restaurant){
        val userName = findViewById<TextView>(R.id.profileUserName)
        val description = findViewById<TextView>(R.id.profileDescription)

        userName.text = restaurant.getName()
        description.text = restaurant.getAddress() + " " + restaurant.getAddressNum()

        val imageUrl = restaurant.getMultimedia().getImage()

        Glide.with(this)
            .load(imageUrl)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    val backgroundPfp = findViewById<CoordinatorLayout>(R.id.coordinatorLayoutRESPF)
                    backgroundPfp.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun settingsClick(optionsButton: ImageView){
        optionsButton.setOnClickListener{
            val fragment = SettingsFrag()
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.alpha_show_up)

            supportFragmentManager.beginTransaction()
                .replace(R.id.optionsScreen, fragment)
                .addToBackStack(null)
                .commit()
            val optionsScreen = findViewById<FragmentContainerView>(R.id.optionsScreen)
            val opaqueBg = findViewById<View>(R.id.opaqueBg)

            optionsScreen.startAnimation(fadeIn)
            opaqueBg.startAnimation(fadeIn)
            opaqueBg.visibility = View.VISIBLE
            optionsScreen.visibility = View.VISIBLE
        }
    }

    private fun editProfileClick(editButton: ImageView) {
        editButton.setOnClickListener {
            val fragment = EditUserFrag()
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.alpha_show_up)

            supportFragmentManager.beginTransaction()
                .replace(R.id.editUserScreen, fragment)
                .addToBackStack(null)
                .commit()
            val editUserScreen = findViewById<FragmentContainerView>(R.id.editUserScreen)
            val opaqueBg = findViewById<View>(R.id.opaqueBg)

            editUserScreen.startAnimation(fadeIn)
            opaqueBg.startAnimation(fadeIn)
            opaqueBg.visibility = View.VISIBLE
            editUserScreen.visibility = View.VISIBLE
        }
    }
}