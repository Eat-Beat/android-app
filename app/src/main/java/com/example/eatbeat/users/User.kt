package com.example.eatbeat.users

abstract class User(
    private val idUser: Int,
    private val name: String,
    private val email: String,
    private val password: String,
    private val rating: Float
){
    fun getName(): String {
        return name
    }

    fun getPassword(): String {
        return password
    }
}