package com.example.eatbeat.users

import android.location.Geocoder
import android.os.Parcel
import android.os.Parcelable
import com.example.eatbeat.users.musicianAttributes.Multimedia
import java.util.Locale

class Musician(
    idUser: Int,
    idRol: Int,
    name: String,
    email: String,
    password: String,
    private val rating: Float,
    private val longitude: Float,
    private val latitude: Float,
    private val description: String,
    private val multimedia: ArrayList<Multimedia> = ArrayList(),
    private val genre: ArrayList<String> = ArrayList(),
    private val classification: ArrayList<String> = ArrayList()
) : User(idUser, idRol, name, email, password), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString()!!,
        ArrayList<Multimedia>().apply {
            parcel.readList(this, Multimedia::class.java.classLoader)
        },
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
                                      ) {
    }

    fun getMultimedia(): ArrayList<Multimedia> {
        return multimedia
    }

    fun getRating(): Float{
        return this.rating
    }

    fun getDescription(): String{
        return this.description
    }

    fun getGenre(): ArrayList<String> {
        return genre
    }

    fun getClassification(): ArrayList<String> {
        return classification
    }

    fun getLongitude(): Float{
        return longitude
    }

    fun getLatitude(): Float{
        return latitude
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(longitude)
        parcel.writeFloat(latitude)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Musician> {
        override fun createFromParcel(parcel: Parcel): Musician {
            return Musician(parcel)
        }

        override fun newArray(size: Int): Array<Musician?> {
            return arrayOfNulls(size)
        }
    }


}