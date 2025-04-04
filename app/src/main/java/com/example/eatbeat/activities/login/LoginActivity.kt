package com.example.eatbeat.activities.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eatbeat.R
import com.example.eatbeat.activities.main.SearchMusicianActivity
import com.example.eatbeat.activities.main.SearchRestaurantActivity
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.User
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.api.ApiRepository.getUsers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val userName = findViewById<TextView>(R.id.usernameTextbox)
        val password = findViewById<TextView>(R.id.passwordTextbox)
        var userList = ArrayList<User>()

        lifecycleScope.launch {
            try {
                val users = getUsers()
                userList = users?.toMutableList() as ArrayList<User>
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        loginButton.setOnClickListener(){
            if (verifyUser(userName.text.toString(), password.text.toString(), userList)){
                when(UserData.userType){
                    1 -> {
                        val intent = Intent(this, SearchRestaurantActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    2 -> {
                        val intent = Intent(this, SearchMusicianActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }else {
                showIncorrectCredentialsMessage(this)
            }
        }
    }

    private fun verifyUser(userName : String, password : String, userList : ArrayList<User>) : Boolean{
        var userFound = false

        for (user in userList) {
//            if (user.getName() == userName && BCrypt.checkpw(password, user.getPassword())) {
//                userFound = true
//                UserData.userId = user.getId()
//                UserData.userType = user.getIdRol()
//            }
            if (userName == user.getEmail() && password == user.getPassword()) {
                userFound = true
                UserData.userId = user.getId()
                UserData.userType = user.getIdRol()
            }
        }
        return userFound
    }

    private fun showIncorrectCredentialsMessage(context: Context) {
        Toast.makeText(context, context.getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show()
    }
}