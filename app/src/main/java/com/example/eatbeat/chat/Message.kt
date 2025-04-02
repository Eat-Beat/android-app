package com.example.eatbeat.chat

import java.util.Date

class Message (
    val idRestaurant: Int,
    val idMusician: Int,
    val idSender: Int,
    val isMultimedia: Boolean,
    val message: String,
    val timeStamp: Date
)