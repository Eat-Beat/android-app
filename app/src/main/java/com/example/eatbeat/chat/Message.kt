package com.example.eatbeat.chat

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Message (
    val idRestaurant: Int,
    val idMusician: Int,
    val idSender: Int,
    val isMultimedia: Boolean,
    val message: String,
    val timestamp: String
){
    fun getDate(): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return formatter.parse(timestamp) ?: Date()
    }
}