package com.example.eatbeat.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.User
import com.example.eatbeat.utils.api.ApiRepository.createChat
import com.example.eatbeat.utils.api.ApiRepository.getChatByRestaurantIdAndMusicianId
import com.example.eatbeat.utils.api.ApiRepository.getChatsByMusicianId
import com.example.eatbeat.utils.api.ApiRepository.getChatsByRestaurantId
import com.example.eatbeat.utils.api.ApiRepository.getUsers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.concurrent.thread

class ChatViewModel(private val idReceaver: Int) : ViewModel() {
    private var tempMessages: List<Message>? = emptyList()

    val messageList by lazy {
        mutableStateListOf<Message>()
    }

    init {
        receaveMessage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendMessage(userInput: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val now = LocalDateTime.now().format(formatter)

      if (UserData.userType == 1) {
            val msg = Message(
                idReceaver,
                UserData.userId,
                UserData.userId,
                false,
                userInput,
                now
                             )
            messageList.add(msg)
            createChat(msg)
        } else {
            val msg = Message(
                UserData.userId,
                idReceaver,
                UserData.userId,
                false,
                userInput,
                now
                             )
            messageList.add(msg)
            createChat(msg)
        }
    }

    fun receaveMessage() {
        viewModelScope.launch {
            while (true) {
                fetchMessages()
                delay(3000)
            }
        }
    }

    private suspend fun fetchMessages() {
        try {
            if (UserData.userType == 1) {
                tempMessages = getChatByRestaurantIdAndMusicianId(idReceaver, UserData.userId)
            } else {
                tempMessages = getChatByRestaurantIdAndMusicianId(UserData.userId, idReceaver)
            }

            if (tempMessages!!.size > messageList.size) {
                tempMessages?.let {
                    messageList.clear()
                    messageList.addAll(it.sortedBy { msg -> msg.getDate()})
                }
            }
        } catch (e: Exception) {
            println("API Connexion Error")
        }
    }

}