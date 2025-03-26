package com.example.eatbeat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eatbeat.Chatbot.MainChatBotActivity

class ChatActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val navChat = findViewById<ImageView>(R.id.navChatIcon)

        navChat.setImageResource(R.drawable.chat_selected)

        activateNavBar()
        activateChatBotBanner()
    }

    private fun activateChatBotBanner() {
        val chatbotBanner= findViewById<ConstraintLayout>(R.id.chatbotid)
        chatbotBanner.setOnClickListener(){
            val intent = Intent(this, MainChatBotActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun activateNavBar(){
        val navSearch = findViewById<ImageView>(R.id.navMusicianIcon)
        val navCalendar = findViewById<ImageView>(R.id.navCalendarIcon)
        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)

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

        navProfile.setOnClickListener(){
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
            finish()
        }
    }
}