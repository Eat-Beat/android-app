package com.example.eatbeat.activities.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
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
import com.example.eatbeat.chat.ProfileCell
import com.example.eatbeat.data.UserData
import com.example.eatbeat.fragments.StatsFrag
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.maps.extension.style.image.image
import kotlinx.coroutines.launch

class ViewMusicianActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_musician)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val musicianId = intent.getIntExtra("musicianId", -1)

        lifecycleScope.launch {
            try {
                val musicians = getMusicians()
                val musiciansList = musicians?.toMutableList() as ArrayList<Musician>
                val musician = musiciansList.find { it.getId() == musicianId }

                if (musician != null){
                    loadInfo(musician)
                    chargeMultimedia(musician)
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        val bottomSheet = findViewById<View>(R.id.profilesheet)
        val messageButton = findViewById<ImageView>(R.id.chatIcon)

        /**
         * Initiates a chat with the user.
         */
        messageButton.setOnClickListener {
            val intent = Intent(this, MainChatActivity::class.java)
            intent.putExtra("userId", musicianId)
            startActivity(intent)
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        val ratingsButton = findViewById<TextView>(R.id.ratingCount)

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 530
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        backButton.setOnClickListener {
            finish()
        }

        statsScreenClick(ratingsButton)
    }

    /**
     * Charges the multimedia of the musician.
     */
    private fun chargeMultimedia(musician: Musician) {
        val multimediaRecycler = findViewById<RecyclerView>(R.id.multimediaUserRecyclerView)

        if (musician.getMultimedia().isNotEmpty()) {
            val imageUrl = musician.getMultimedia()[0].getImage()
            val profileImage = findViewById<ImageView>(R.id.profileImage)

            Glide.with(this)
                .load(imageUrl)
                .into(profileImage)
        } else {
            val backgroundPfp = findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            backgroundPfp.setBackgroundResource( R.drawable.user_selected)
        }

        multimediaRecycler.layoutManager = GridLayoutManager(this, 3)

        multimediaRecycler.adapter = MultimediaAdapter(musician)
    }

    /**
     * Activates the stats fragment when clicked.
     */
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

    /**
     * Loads the musician's info to the screen.
     */
    private fun loadInfo(musician: Musician){
        val profileUserName = findViewById<TextView>(R.id.profileUserName)
        val profileDescription = findViewById<TextView>(R.id.profileDescription)
        val rating = findViewById<RatingBar>(R.id.rating)
        val ratingCount = findViewById<TextView>(R.id.ratingCount)

        profileUserName.text = musician.getName()
        profileDescription.text = musician.getDescription()
        rating.rating = musician.getRating()
        ratingCount.text = "(" + musician.getRating().toString() + ") | SEE DETAILS"
    }
}