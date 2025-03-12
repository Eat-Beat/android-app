package com.example.eatbeat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView

class ContractsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contracts_calendar)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val navContractIc = findViewById<ImageView>(R.id.navCalendarIcon)
        navContractIc.setImageResource(R.drawable.contracts_selected)

        val calendarView : CalendarView = findViewById(R.id.calendarView)
        calendarView.setCalendarDayLayout(R.layout.day_cell)

        activateNavBar()
    }

    private fun activateNavBar(){
        val navSearch = findViewById<ImageView>(R.id.navMusicianIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)

        navSearch.setOnClickListener(){
            val intent = Intent(this, SearchMusicianActivity::class.java)
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
}