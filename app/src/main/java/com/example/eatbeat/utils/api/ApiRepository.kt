package com.example.eatbeat.utils.api

import com.example.eatbeat.chat.Message
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.contracts.PerformProfile
import com.example.eatbeat.users.*
import com.example.eatbeat.users.musicianAttributes.Multimedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiRepository {
    private val apiService = ApiClient.apiService

    suspend fun getUsers(): List<User>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getUsers()
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getRestaurants(): List<Restaurant>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getRestaurants()
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getRestaurantById(id: Int): Restaurant? = withContext(Dispatchers.IO) {
        val response = apiService.getRestaurantById(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getMusicians(): List<Musician>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getMusicians()
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getMusicianById(id: Int): Musician? = withContext(Dispatchers.IO) {
        val response = apiService.getMusicianById(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getPerforms(): List<Perform>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getPerforms()
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getPerformsByMusicianId(id: Int): List<PerformProfile>? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getPerformsByMusicianId(id)
            if (response.isSuccessful) response.body() else null
        }
    }

    suspend fun getPerformsByRestaurantId(id: Int): List<PerformProfile>? = withContext(Dispatchers.IO) {
        val response = apiService.getPerformsByRestaurantId(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun updateMultimedia(id: Int, multimedia: Multimedia): Multimedia? = withContext(Dispatchers.IO) {
        val response = apiService.updateMultimedia(id, multimedia)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun createMultimedia(multimedia: Multimedia): Multimedia? = withContext(Dispatchers.IO) {
        val response = apiService.createMultimedia(multimedia)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteMultimedia(id: Int): Boolean = withContext(Dispatchers.IO) {
        val response = apiService.deleteMultimedia(id)
        response.isSuccessful
    }

    suspend fun getChatsByRestaurantId(id: Int): List<Message>? = withContext(Dispatchers.IO) {
        val response = apiService.getChatsByRestaurantId(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getChatsByMusicianId(id: Int): List<Message>? = withContext(Dispatchers.IO) {
        val response = apiService.getChatsByMusicianId(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getChatByRestaurantIdAndMusicianId(restaurantId: Int, musicianId: Int): List<Message>? = withContext(Dispatchers.IO) {
        val response = apiService.getChatByRestaurantIdAndMusicianId(restaurantId, musicianId)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun createChat(chat: Message): Message? = withContext(Dispatchers.IO) {
        val response = apiService.createChat(chat)
        if (response.isSuccessful) response.body() else null
    }

    // USERS
    suspend fun createUser(user: User): User? = withContext(Dispatchers.IO) {
        val response = apiService.createUser(user)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun updateUser(id: Int, user: User): User? = withContext(Dispatchers.IO) {
        val response = apiService.updateUser(id, user)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteUser(id: Int): Boolean = withContext(Dispatchers.IO) {
        apiService.deleteUser(id).isSuccessful
    }

    // MUSICIANS
    suspend fun createMusician(musician: Musician): Musician? = withContext(Dispatchers.IO) {
        val response = apiService.createMusician(musician)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun updateMusician(id: Int, musician: Musician): Musician? = withContext(Dispatchers.IO) {
        val response = apiService.updateMusician(id, musician)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteMusician(id: Int): Boolean = withContext(Dispatchers.IO) {
        apiService.deleteMusician(id).isSuccessful
    }

    // RESTAURANTS
    suspend fun createRestaurant(restaurant: Restaurant): Restaurant? = withContext(Dispatchers.IO) {
        val response = apiService.createRestaurant(restaurant)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun updateRestaurant(id: Int, restaurant: Restaurant): Restaurant? = withContext(Dispatchers.IO) {
        val response = apiService.updateRestaurant(id, restaurant)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteRestaurant(id: Int): Boolean = withContext(Dispatchers.IO) {
        apiService.deleteRestaurant(id).isSuccessful
    }
}