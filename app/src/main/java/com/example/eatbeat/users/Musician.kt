package com.example.eatbeat.users

import com.example.eatbeat.users.musicianAttributes.Classification
import com.example.eatbeat.users.musicianAttributes.Genre
import com.example.eatbeat.users.musicianAttributes.Multimedia

class Musician(
    idUser: Int,
    name: String,
    email: String,
    password: String,
    ratings: ArrayList<Int>,
    private val longitude: Float,
    private val latitude: Float,
    private val description: String,
    private val multimedia: ArrayList<Multimedia> = ArrayList(),
    private val genre: ArrayList<Genre> = ArrayList(),
    private val classification: ArrayList<Classification> = ArrayList()
) : User(idUser, name, email, password, ratings) {

}