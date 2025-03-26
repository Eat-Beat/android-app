package com.example.eatbeat.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.example.eatbeat.activities.ChatActivity
import com.example.eatbeat.activities.contracts.ContractsCalendarActivity
import com.example.eatbeat.R
import com.example.eatbeat.activities.main.SearchMusicianActivity
import com.example.eatbeat.activities.main.SearchRestaurantActivity
import com.example.eatbeat.activities.profile.MusicianProfile
import com.example.eatbeat.data.UserData

fun activateNavBar(activity: Activity, context: Context, selected: Int){
    val navUser = activity.findViewById<ImageView>(R.id.navUserIcon)
    val navCalendar = activity.findViewById<ImageView>(R.id.navCalendarIcon)
    val navChat = activity.findViewById<ImageView>(R.id.navChatIcon)
    val navProfile = activity.findViewById<ImageView>(R.id.navProfileIcon)

    if(UserData.userType == 1){
        navUser.setImageResource(R.drawable.restaurant)
        navUser.setOnClickListener{
            val intent = Intent(context, SearchRestaurantActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }

    }else if(UserData.userType == 2){
        navUser.setImageResource(R.drawable.musician)
        navUser.setOnClickListener{
            val intent = Intent(context, SearchMusicianActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    when(selected){
        1 -> if (UserData.userType == 1){
            navUser.setImageResource(R.drawable.restaurant_selected)
        } else if (UserData.userType == 2){
            navUser.setImageResource(R.drawable.musician_selected)
        }
        2 -> navCalendar.setImageResource(R.drawable.contracts_selected)
        3 -> navChat.setImageResource(R.drawable.chat_selected)
        4 -> navProfile.setImageResource(R.drawable.user_selected)
    }

    navCalendar.setOnClickListener{
        val intent = Intent(context, ContractsCalendarActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    navChat.setOnClickListener{
        val intent = Intent(context, ChatActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    navProfile.setOnClickListener{
        val intent = Intent(context, MusicianProfile::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}