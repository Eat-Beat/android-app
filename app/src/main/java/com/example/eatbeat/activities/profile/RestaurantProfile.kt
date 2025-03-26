package com.example.eatbeat.activities.profile

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.example.eatbeat.R
import com.example.eatbeat.fragments.EditUserFrag
import com.example.eatbeat.fragments.SettingsFrag
import com.example.eatbeat.utils.activateNavBar
import com.google.android.material.bottomsheet.BottomSheetBehavior

class RestaurantProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_musician)
        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val optionsButton = findViewById<ImageView>(R.id.settings_icon)
        val editButton = findViewById<ImageView>(R.id.editUserButton)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 530
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)
        navProfile.setImageResource(R.drawable.user_selected)

        settingsClick(optionsButton)
        editProfileClick(editButton)

        activateNavBar(this, this, 4)
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