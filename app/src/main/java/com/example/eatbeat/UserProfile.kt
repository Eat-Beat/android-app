package com.example.eatbeat

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_musician)
        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val optionsButton = findViewById<ImageView>(R.id.settings_icon)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 330
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        settingsClick(optionsButton)


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

            optionsScreen.startAnimation(fadeIn)
            optionsScreen.visibility = View.VISIBLE
        }
    }
}