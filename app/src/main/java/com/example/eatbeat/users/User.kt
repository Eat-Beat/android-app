package com.example.eatbeat.users

abstract class User(
    private val idUser: Int,
    private val name: String,
    private val email: String,
    private val password: String,
    private val ratings: ArrayList<Int>
){
    fun getName(): String {
        return name
    }

    fun getPassword(): String {
        return password
    }
}