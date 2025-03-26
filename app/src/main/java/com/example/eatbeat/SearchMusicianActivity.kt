package com.example.eatbeat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.adapters.MusicianAdapter
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.loadMusiciansFromJson
import com.example.eatbeat.utils.loadJsonFromRaw

class SearchMusicianActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_musician)
        val navMusicianIcon = findViewById<ImageView>(R.id.navMusicianIcon)
        navMusicianIcon.setImageResource(R.drawable.musician_selected)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        activateNavBar()
        showMusicians(loadMusiciansFromJson(loadJsonFromRaw(this, R.raw.musicians)!!))
    }

    private fun activateNavBar(){
        val navCalendar = findViewById<ImageView>(R.id.navCalendarIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)

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

        navProfile.setOnClickListener(){
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showMusicians(musicianList : List<Musician>){
        val musicianRecycler = findViewById<RecyclerView>(R.id.searchMusicRecyclerView)

        musicianRecycler.layoutManager = GridLayoutManager(this, 2)

        musicianRecycler.adapter = MusicianAdapter(musicianList)
    }
}