package com.example.eatbeat.Chatbot


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatbeat.ui.theme.bgColor
import com.example.eatbeat.ui.theme.darkbgColor
import com.example.eatbeat.ui.theme.orange

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
          ) {
        AppHeader()
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    LazyColumn(
        modifier = modifier, reverseLayout = true
              ) {
        items(messageList.reversed()) {
            MessageRow(messageModel = it)
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically
       ) {
        Box(
            modifier = Modifier.fillMaxWidth()
           ) {
            Box(
                modifier = Modifier
                    .align(
                        if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                          )
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                            )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) darkbgColor else orange)
                    .padding(16.dp)
               ) {
                Text(
                    text = messageModel.message,
                    fontWeight = FontWeight.W500,
                    color = if (isModel) Color.White else darkbgColor
                    )
            }
        }
    }
}


@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {

    var message by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically
       ) {
        OutlinedTextField(value = message, onValueChange = {
            message = it
        })
        IconButton(onClick = {
            onMessageSend(message)
            message = ""
        }) {

            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")

        }
    }
}

@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(darkbgColor)
       ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Easy Bot",
            color = Color.White,
            fontSize = 22.sp
            )

    }
}