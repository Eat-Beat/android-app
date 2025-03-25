package com.example.eatbeat.contracts

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Perform(
               private val idPerform: Int,
               private val idRestaurant: Int,
               private val idMusician: Int,
               val dateTime: Date,
               private val budget: Int,
               private val musicianRate : Int,
               val restaurantRate: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        Date(parcel.readLong()),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
                                      )

    fun getDate(): Date {
        return this.dateTime
    }

    fun getIdMusician(): Int {
        return this.idMusician
    }

    fun getIdRestaurant(): Int {
        return this.idRestaurant
    }

    fun getRatingRestaurant(): Int {
        return this.restaurantRate
    }

    fun getCost(): Int {
        return this.budget
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idPerform)
        parcel.writeInt(idRestaurant)
        parcel.writeInt(idMusician)
        parcel.writeInt(budget)
        parcel.writeInt(musicianRate)
        parcel.writeInt(restaurantRate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Perform> {
        override fun createFromParcel(parcel: Parcel): Perform {
            return Perform(parcel)
        }

        override fun newArray(size: Int): Array<Perform?> {
            return arrayOfNulls(size)
        }
    }
}