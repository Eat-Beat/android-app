package com.example.eatbeat.data

import com.example.eatbeat.R
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson

object UserData {
    var userId: Int = 0
    var userType: Int = 0
    var musicians: List<Musician> = listOf()
}