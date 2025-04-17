package com.example.eatbeat.activities.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.eatbeat.R
import com.example.eatbeat.users.User

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_user)

        val continueButton = findViewById<Button>(R.id.continueButton)
        val loginButton = findViewById<TextView>(R.id.loginButton)
        val name = findViewById<TextView>(R.id.nameTextbox)
        val email = findViewById<TextView>(R.id.emailTextbox)
        val newPassword = findViewById<TextView>(R.id.newPasswordTextbox)
        val confirmPassword = findViewById<TextView>(R.id.confirmPasswordTextbox)
        val radioGroupUserType = findViewById<RadioGroup>(R.id.radioGroupUserType)

        loginButton.setOnClickListener(){
            finish()
        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

        continueButton.setOnClickListener(){
            if (checkData(name.text.toString(), email.text.toString(), newPassword.text.toString(),
                    confirmPassword.text.toString(), radioGroupUserType.checkedRadioButtonId,
                    this)){
                val user = User(-1, -1, name.text.toString(), email.text.toString(),
                    newPassword.text.toString())
                when(radioGroupUserType.checkedRadioButtonId){
                    R.id.musicianRadioButton -> {
                        user.setIdRol(1)
                        val intent = Intent(this, NewMusicianActivity::class.java)
                        intent.putExtra("user", user)
                        launcher.launch(intent)
                    }
                    R.id.restaurantRadioButton -> {
                        user.setIdRol(2)
                        val intent = Intent(this, NewRestaurantActivity::class.java)
                        intent.putExtra("user", user)
                        launcher.launch(intent)
                    }
                }
            }
        }
    }

    private fun checkData(name : String, email : String, password : String,
                          confirmPassword : String, userType : Int, context: Context) : Boolean{
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
            confirmPassword.isEmpty() || userType == -1 || password != confirmPassword){
            Toast.makeText(context, context.getString(R.string.incorrect_credentials),
                Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }
}