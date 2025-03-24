package com.example.eatbeat.users

import com.example.eatbeat.users.musicianAttributes.Multimedia

class Restaurant(
    idUser: Int,
    name: String,
    email: String,
    password: String,
    rating: Float,
    private val address: String,
    private val addressNum: Int,
    private val zipCode: Int,
    private val multimedia: ArrayList<Multimedia> = ArrayList()
    ) : User(idUser, name, email, password, rating){


    fun getMultimedia(): ArrayList<Multimedia> {
        return multimedia
    }

    }