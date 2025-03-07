package com.example.eatbeat.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eatbeat.R
import com.example.eatbeat.SearchMusicianActivity
import com.example.eatbeat.users.User
import org.mindrot.jbcrypt.BCrypt

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        val loginButton = findViewById<Button>(R.id.loginButton)
        val userName = findViewById<TextView>(R.id.usernameTextbox)
        val password = findViewById<TextView>(R.id.passwordTextbox)

        loginButton.setOnClickListener(){
            val intent = Intent(this, SearchMusicianActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun verifyUser(userName : TextView, password : TextView, userList : ArrayList<User>) : Boolean{
        var foundUser = false

        for (user in userList) {
            if (user.getName() == userName.text && BCrypt.checkpw(password.text.toString(), user.getPassword())) {
                foundUser = true
            }
        }

        return foundUser
    }

}