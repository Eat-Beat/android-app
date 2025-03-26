package com.example.eatbeat.users

open class User(
    private val idUser: Int,
    private val idRol: Int,
    private val name: String,
    private val email: String,
    private val password: String,
){
    fun getId(): Int{
        return this.idUser
    }

    fun getIdRol(): Int{
        return this.idRol
    }

    fun getName(): String{
        return this.name
    }

    fun getEmail(): String{
        return this.email
    }

    fun getPassword(): String{
        return this.password
    }
}