package com.example.eatbeat.utils.api

import com.example.eatbeat.chat.Message
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.contracts.PerformProfile
import com.example.eatbeat.users.*
import com.example.eatbeat.users.musicianAttributes.Multimedia
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("restaurants")
    suspend fun getRestaurants(): Response<List<Restaurant>>

    @GET("performs")
    suspend fun getPerforms(): Response<List<Perform>>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: Int): Response<Restaurant>

    @GET("musicians")
    suspend fun getMusicians(): Response<List<Musician>>

    @GET("musicians/{id}")
    suspend fun getMusicianById(@Path("id") id: Int): Response<Musician>

    @GET("performs/profile/musician/{id}")
    suspend fun getPerformsByMusicianId(@Path("id") id: Int): Response<List<PerformProfile>>

    @GET("performs/restaurant/{id}")
    suspend fun getPerformsByRestaurantId(@Path("id") id: Int): Response<List<PerformProfile>>

    @PUT("multimedias/{id}")
    suspend fun updateMultimedia(@Path("id") id: Int, @Body multimedia: Multimedia): Response<Multimedia>

    @POST("multimedias")
    suspend fun createMultimedia(@Body multimedia: Multimedia): Response<Multimedia>

    @DELETE("multimedias/{id}")
    suspend fun deleteMultimedia(@Path("id") id: Int): Response<Void>

    @GET("chats/restaurant/{id}")
    suspend fun getChatsByRestaurantId(@Path("id") id: Int): Response<List<Message>>

    @GET("chats/musician/{id}")
    suspend fun getChatsByMusicianId(@Path("id") id: Int): Response<List<Message>>

    @GET("chats/{restaurantId}/{musicianId}")
    suspend fun getChatByRestaurantIdAndMusicianId(@Path("restaurantId") restaurantId: Int, @Path("musicianId") musicianId: Int): Response<List<Message>>

    @POST("chats")
    suspend fun createChat(@Body chat: Message): Response<Message>
}