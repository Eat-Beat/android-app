package com.example.eatbeat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.example.eatbeat.fragments.EditUserFrag
import com.example.eatbeat.fragments.SettingsFrag
import com.example.eatbeat.fragments.StatsFrag
import com.google.android.material.bottomsheet.BottomSheetBehavior

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_musician)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val optionsButton = findViewById<ImageView>(R.id.settings_icon)
        val editButton = findViewById<ImageView>(R.id.editUserButton)
        val ratingsButton = findViewById<TextView>(R.id.ratingCount)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 530
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)
        navProfile.setImageResource(R.drawable.profile_selected_ic)

        settingsClick(optionsButton)
        editProfileClick(editButton)
        statsScreenClick(ratingsButton)

        activateNavBar()
    }

    private fun activateNavBar(){
        val navSearch = findViewById<ImageView>(R.id.navMusicianIcon)
        val navCalendar = findViewById<ImageView>(R.id.navCalendarIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)

        navSearch.setOnClickListener(){
            val intent = Intent(this, SearchMusicianActivity::class.java)
            startActivity(intent)
            finish()
        }

        navCalendar.setOnClickListener(){
            val intent = Intent(this, ContractsActivity::class.java)
            startActivity(intent)
            finish()
        }

        navChat.setOnClickListener(){
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }
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

    private fun statsScreenClick(statsButton: TextView) {
        statsButton.setOnClickListener {
            val fragment = StatsFrag()
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.alpha_show_up)

            supportFragmentManager.beginTransaction()
                .replace(R.id.ratingsScreen, fragment)
                .addToBackStack(null)
                .commit()
            val ratingsScreen = findViewById<FragmentContainerView>(R.id.ratingsScreen)
            val opaqueBg = findViewById<View>(R.id.opaqueBg)

            ratingsScreen.startAnimation(fadeIn)
            opaqueBg.startAnimation(fadeIn)
            opaqueBg.visibility = View.VISIBLE
            ratingsScreen.visibility = View.VISIBLE
        }
    }
}