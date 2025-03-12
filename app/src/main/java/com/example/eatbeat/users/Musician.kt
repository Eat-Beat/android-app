package com.example.eatbeat.users

import com.example.eatbeat.users.musicianAttributes.Multimedia

class Musician(
    idUser: Int,
    name: String,
    email: String,
    password: String,
    rating: Float,
    private val longitude: Float,
    private val latitude: Float,
    private val description: String,
    private val multimedia: ArrayList<Multimedia> = ArrayList(),
    private val genre: ArrayList<String> = ArrayList(),
    private val classification: ArrayList<String> = ArrayList()
) : User(idUser, name, email, password, rating) {

    fun getMultimedia(): ArrayList<Multimedia> {
        return multimedia
    }

    fun getGenre(): ArrayList<String> {
        return genre
    }

    fun calculateLocationName(): String{
        return longitude.toString() + " " + latitude.toString()
    }

    fun calculateRating(): Int {
        val totalRating : Int = 0

        return totalRating
    }
}