package com.example.eatbeat.utils

import android.content.Context
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat

fun loadJsonFromRaw(context: Context, rawResId: Int): String? {
    return try {
        val inputStream = context.resources.openRawResource(rawResId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        reader.use { it.forEachLine { line -> stringBuilder.append(line).append("\n") } }

        stringBuilder.toString().trim()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun loadRestaurantsFromJson(json: String): ArrayList<Restaurant> {
    val gson = Gson()
    val restaurantListType = object : com.google.gson.reflect.TypeToken<ArrayList<Restaurant>>() {}.type
    return gson.fromJson(json, restaurantListType)
}

fun loadContractsFromJson(json: String): ArrayList<Perform> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    val gson = GsonBuilder()
        .setDateFormat(dateFormat.toPattern())
        .create()

    val contractsListType = object : TypeToken<ArrayList<Perform>>() {}.type

    return gson.fromJson(json, contractsListType)
}

fun loadMusiciansFromJson(json: String): ArrayList<Musician> {
    val gson = Gson()
    val restaurantListType = object : com.google.gson.reflect.TypeToken<ArrayList<Musician>>() {}.type
    return gson.fromJson(json, restaurantListType)
}
