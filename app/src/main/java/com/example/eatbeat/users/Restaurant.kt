package com.example.eatbeat.users

import com.example.eatbeat.users.musicianAttributes.Multimedia

class Restaurant(
    idUser: Int,
    idRol: Int,
    name: String,
    email: String,
    password: String,
    private val rating: Float,
    private val address: String,
    private val addressNum: Int,
    private val zipCode: Int,
    private val multimedia: ArrayList<Multimedia> = ArrayList()
    ) : User(idUser, idRol, name, email, password){


    fun getMultimedia(): ArrayList<Multimedia> {
        return multimedia
    }

    fun getRating(): String{
        return this.rating.toString()
    }

    }