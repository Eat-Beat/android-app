package com.example.eatbeat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.chatbot.MainChatBotActivity
import com.example.eatbeat.R
import com.example.eatbeat.adapters.ChatsAdapter
import com.example.eatbeat.chat.MainChatActivity
import com.example.eatbeat.chat.Message
import com.example.eatbeat.chat.ProfileCell
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getChatsByMusicianId
import com.example.eatbeat.utils.api.ApiRepository.getChatsByRestaurantId
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.loadChatsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        activateNavBar(this,this,3)
        activateChatBotBanner()

        lifecycleScope.launch {
            try {
                val messages = if (UserData.userType == 1) {
                    getChatsByMusicianId(UserData.userId)
                } else {
                    getChatsByRestaurantId(UserData.userId)
                }

                val profileCellsRecycler = findViewById<RecyclerView>(R.id.chatsListRecyclerView)
                profileCellsRecycler.layoutManager = LinearLayoutManager(this@ChatActivity)

                if (messages != null) {
                    val profileCells = loadProfileCellsList(messages)
                    profileCellsRecycler.adapter = ChatsAdapter(profileCells) { profileCell ->
                        openChat(profileCell)
                    }
                }
            } catch (e: Exception) {
                println("API Connection Error: ${e.message}")
            }
        }
    }

    private fun openChat(profileCell: ProfileCell) {
        val intent = Intent(this, MainChatActivity::class.java)
        intent.putExtra("userId", profileCell.userId)
        startActivity(intent)
    }

    private suspend fun loadProfileCellsList(messages: List<Message>): ArrayList<ProfileCell> {
        val profileCells = ArrayList<ProfileCell>()

        if (UserData.userType == 1) {
            val restaurants = getRestaurants()
            for (message in messages) {
                val restaurant = restaurants?.find { it.getId() == message.idRestaurant }
                if (restaurant != null) {
                    val profileCell = ProfileCell(
                        message.idRestaurant,
                        restaurant.getName(),
                        restaurant.getMultimedia()
                                                 )
                    if (!profileCells.contains(profileCell)) {
                        profileCells.add(profileCell)
                    }
                }
            }
        } else {
            val musicians = getMusicians()
            for (message in messages) {
                val musician = musicians?.find { it.getId() == message.idMusician }
                if (musician != null) {
                    val profileCell = ProfileCell(
                        message.idMusician,
                        musician.getName(),
                        musician.getMultimedia().first()
                                                 )
                    if (!profileCells.contains(profileCell)) {
                        profileCells.add(profileCell)
                    }
                }
            }
        }
        return profileCells
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