package com.example.eatbeat.contracts

import java.util.Date

class Perform( private val idRestaurant: Int,
               private val idMusician: Int,
               private val idPerform: Int,
               private val dateTime: Date,
               private val budget: Int){

    fun getDate(): Date {
        return this.dateTime
    }

    fun getIdMusician(): Int {
        return this.idMusician
    }
}