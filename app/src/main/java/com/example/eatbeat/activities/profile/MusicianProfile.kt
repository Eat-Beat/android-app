package com.example.eatbeat.activities.profile

import android.graphics.drawable.Drawable
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
import com.example.eatbeat.fragments.EditUserFrag
import com.example.eatbeat.fragments.SettingsFrag
import com.example.eatbeat.fragments.StatsFrag
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MusicianProfile : AppCompatActivity() {
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
        navProfile.setImageResource(R.drawable.user_selected)

        settingsClick(optionsButton)
        editProfileClick(editButton)
        statsScreenClick(ratingsButton)

        activateNavBar(this, this, 4)

        val musicians = loadMusiciansFromJson(loadJsonFromRaw(this, R.raw.musicians)!!)

        chargeMultimedia(musicians[0])
    }

    private fun chargeMultimedia(musician: Musician) {
        val multimediaRecycler = findViewById<RecyclerView>(R.id.multimediaUserRecyclerView)

        val imageUrl = musician.getMultimedia()[0].getImage()

        Glide.with(this)
            .load(imageUrl)
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable, transition: Transition<in Drawable?>?) {
                    val backgroundPfp = findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
                    backgroundPfp.background = resource
                }
            })

        multimediaRecycler.layoutManager = GridLayoutManager(this, 3)

        multimediaRecycler.adapter = MultimediaAdapter(musician)
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