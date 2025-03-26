package com.example.eatbeat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eatbeat.Chatbot.MainChatBotActivity
import com.example.eatbeat.R
import com.example.eatbeat.utils.activateNavBar

class ChatActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val navChat = findViewById<ImageView>(R.id.navChatIcon)

        navChat.setImageResource(R.drawable.chat_selected)

        activateNavBar(this,this,3)
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
}