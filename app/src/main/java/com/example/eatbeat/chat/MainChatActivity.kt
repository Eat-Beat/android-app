package com.example.eatbeat.chat

import android.os.Build
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.eatbeat.ui.theme.GroqTestTheme

class MainChatActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val idReceiver = intent.getIntExtra("userId", -1)
        val factory = ChatViewModelFactory(idReceiver)
        val chatBotViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]
        setContent {
            GroqTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatPage(modifier = Modifier.padding(innerPadding), chatBotViewModel)
                }
            }
        }
    }
}