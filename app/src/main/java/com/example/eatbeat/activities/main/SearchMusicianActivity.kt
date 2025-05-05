package com.example.eatbeat.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.MusicianAdapter
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.loadMusiciansFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import kotlinx.coroutines.launch

class SearchMusicianActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_musician)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

        overridePendingTransition(R.anim.transition_fade_activity, 0)
        activateNavBar(this, this, 1)

        lifecycleScope.launch {
            try {
                val musicians = getMusicians()
                val musiciansList = musicians?.toMutableList() as ArrayList<Musician>
                showMusicians(musiciansList)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    /**
     * Generates the list of musicians on screen.
     */
    private fun showMusicians(musicianList : ArrayList<Musician>){
        val musicianRecycler = findViewById<RecyclerView>(R.id.searchMusicRecyclerView)

        musicianRecycler.layoutManager = GridLayoutManager(this, 2)

        musicianRecycler.adapter = MusicianAdapter(musicianList){musician -> openProfile(musician)}
    }

    /**
     * When clicking a musician, it will open its profile.
     */
    private fun openProfile(musician: Musician){
        val intent = Intent(this, ViewMusicianActivity::class.java)
        intent.putExtra("musicianId", musician.getId())
        startActivity(intent)
    }
}