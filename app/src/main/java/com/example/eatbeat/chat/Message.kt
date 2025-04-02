package com.example.eatbeat.chat

import java.sql.Date

class Message(
    private val idRestaurant: Int,
    private val idMusician: Int,
    private val idSender: Int,
    private val isMultimedia: Boolean,
    private val message: String,
    private val timestamp: Date
) {


}