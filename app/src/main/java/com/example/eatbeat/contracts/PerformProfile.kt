package com.example.eatbeat.contracts

import android.os.Parcelable
import java.util.Date

class PerformProfile (
    private val name: String,
    private val address: String,
    private val addressNum: Int,
    val zipCode: String,
    private val rate: Int) {


    fun getName(): String {
        return this.name
    }
    fun getRate(): Int {
        return this.rate
    }


}