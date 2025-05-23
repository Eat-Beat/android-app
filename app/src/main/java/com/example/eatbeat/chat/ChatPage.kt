package com.example.eatbeat.chat


import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatbeat.activities.ChatActivity
import com.example.eatbeat.R
import com.example.eatbeat.data.UserData
import com.example.eatbeat.ui.theme.bgColor
import com.example.eatbeat.ui.theme.darkbgColor
import com.example.eatbeat.ui.theme.orange
import kotlinx.coroutines.launch



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatPage(
    modifier: Modifier = Modifier, viewModel: ChatViewModel, onBackClick: () -> Unit = {}
                   ) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
          ) {
        AppHeader(onBackClick = {
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        })
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        MessageInput(onMessageSend = { message ->
            coroutineScope.launch {
                viewModel.sendMessage(message)
            }
        })
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<Message>) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
              ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Icon",
                tint = orange,
                )
            Text(text = "Start the Conversation!", fontSize = 22.sp)
        }
    } else {
        LazyColumn(
            modifier = modifier, reverseLayout = true
                  ) {
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: Message) {
    val isSender = if (UserData.userType== 1) messageModel.idSender == messageModel.idRestaurant
    else messageModel.idSender == messageModel.idMusician
    Row(
        verticalAlignment = Alignment.CenterVertically
       ) {
        Box(
            modifier = Modifier.fillMaxWidth()
           ) {
            Box(
                modifier = Modifier
                    .align(
                        if (isSender) Alignment.BottomStart else Alignment.BottomEnd
                          )
                    .padding(
                        start = if (isSender) 8.dp else 70.dp,
                        end = if (isSender) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                            )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isSender) darkbgColor else orange)
                    .padding(16.dp)
               ) {
                Text(
                    text = messageModel.message,
                    fontWeight = FontWeight.W500,
                    color = if (isSender) Color.White else darkbgColor
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
       ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f)
                         )
        IconButton(
            onClick = {
                if (message.isNotBlank()) {
                    onMessageSend(message.trim())
                    message = ""
                }
            }, modifier = Modifier.padding(start = 8.dp)
                  ) {

            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")

        }
    }
}

@Composable
fun AppHeader(modifier: Modifier = Modifier, onBackClick: () -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(darkbgColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
       ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
                )
        }
        val headerText: String;
        if (UserData.userType== 1)
            headerText = "Restaurant"
        else
            headerText= "Musician"
        Text(
            text = headerText,
            color = Color.White,
            fontSize = 22.sp,
            modifier = Modifier.padding(start = 8.dp)
            )
    }
}