package com.example.eatbeat.users.musicianAttributes

import android.graphics.Bitmap
import android.media.Image
import android.widget.ImageView

class Multimedia(
    private val idMultimedia: Int,
    private val url: String,
    private val size: Float,
    private val type: String,
) {

    fun getImage(): String {
        return url
    }
}