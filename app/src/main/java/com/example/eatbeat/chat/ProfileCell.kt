package com.example.eatbeat.chat

import com.example.eatbeat.users.musicianAttributes.Multimedia

data class ProfileCell(
    val userId : Int,
    val name : String,
    val profileImage : Multimedia
)