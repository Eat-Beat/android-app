package com.example.eatbeat.users

import android.location.Geocoder
import com.example.eatbeat.users.musicianAttributes.Multimedia
import java.util.Locale

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

    fun getClassification(): ArrayList<String> {
        return classification
    }

    fun calculateLocationName(context: android.content.Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
            if (!addresses.isNullOrEmpty()) {
                return addresses[0].locality ?: "Ubicación desconocida"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "Ubicación desconocida"
    }


}