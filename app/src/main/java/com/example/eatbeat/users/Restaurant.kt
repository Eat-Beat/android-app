package com.example.eatbeat.users

class Restaurant(
    idUser: Int,
    name: String,
    email: String,
    password: String,
    rating: Float,
    private val address: String,
    private val addressNum: Int,
    private val zipCode: Int,
) : User(idUser, name, email, password, rating)  {

}