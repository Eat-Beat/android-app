package com.example.eatbeat.users

import java.io.Serializable

open class User(
    private val idUser: Int,
    private var idRol: Int,
    private val name: String,
    private val email: String,
    private val password: String,
): Serializable {
    fun setIdRol(idRol: Int){
        this.idRol = idRol
    }

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