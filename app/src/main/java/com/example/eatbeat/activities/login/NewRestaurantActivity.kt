package com.example.eatbeat.activities.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.users.User
import com.example.eatbeat.users.musicianAttributes.Multimedia
import com.example.eatbeat.utils.api.ApiRepository
import kotlinx.coroutines.launch

class NewRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_restaurant)

        val user = intent.getSerializableExtra("user") as User
        val createButton = findViewById<Button>(R.id.createButton)
        val backButton = findViewById<TextView>(R.id.backButton)
        val address = findViewById<TextView>(R.id.addressTextbox)
        val number = findViewById<TextView>(R.id.numberTextbox)
        val postalCode = findViewById<TextView>(R.id.postalCodeTextbox)

        backButton.setOnClickListener(){
            finish()
        }

        createButton.setOnClickListener(){
            if (checkData(address.text.toString(), number.text.toString(),
                    postalCode.text.toString(), this)){
                val restaurant = Restaurant(user.getId(), user.getIdRol(), user.getName(),
                    user.getEmail(), user.getPassword(), -1.toFloat(), address.text.toString(),
                    number.text.toString().toInt(), postalCode.text.toString().toInt(),
                    Multimedia(1, "", 1.toFloat(), "photo")
                )
                createUser(user,restaurant)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun createUser(user: User, restaurant: Restaurant) {
        // Usar lifecycleScope para llamar a la función suspendida en el contexto adecuado
        lifecycleScope.launch {
            try {
                // Llamar a la función de creación de usuario
                val createdUser = ApiRepository.createUser(user)
                // Verificar si la respuesta fue exitosa
                if (createdUser != null) {
                    // Aquí puedes manejar la respuesta, como actualizar la UI con el nuevo usuario
                    Toast.makeText(this@NewRestaurantActivity, "Usuario creado exitosamente: $createdUser", Toast.LENGTH_SHORT).show()
                    restaurant.setId(createdUser.getId())
                    ApiRepository.createRestaurant(restaurant)
                } else {
                    // Manejo de error si la creación del usuario falla
                    Toast.makeText(this@NewRestaurantActivity, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Manejo de errores de red o excepciones
                Toast.makeText(this@NewRestaurantActivity, "Error de conexión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkData(address : String, number : String, postalCode : String, context : Context): Boolean{
        if (address.isEmpty() || number.isEmpty() || postalCode.isEmpty()){
            showIncorrectCredentialsMessage(context)
            return false
        }else{
            if (number.toInt() <= 0 || postalCode.toInt() < 1 || postalCode.toInt() > 99999){
                showIncorrectCredentialsMessage(context)
                return false
            }else{
                return true
            }
        }
    }

    private fun showIncorrectCredentialsMessage(context: Context) {
        Toast.makeText(context, context.getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show()
    }
}