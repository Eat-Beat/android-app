package com.example.eatbeat.chatbot
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.eatbeat.ui.theme.GroqTestTheme

class MainChatBotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val chatBotViewModel= ViewModelProvider(this)[ChatBotViewModel::class.java]
        setContent{
            GroqTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatBotChatPage(modifier = Modifier.padding(innerPadding), chatBotViewModel)
                }
            }
        }
    }
}