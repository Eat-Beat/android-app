package com.example.eatbeat.activities.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.User
import com.example.eatbeat.utils.api.ApiRepository
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.launch

class NewMusicianActivity : AppCompatActivity() {
    private lateinit var mapboxMap: MapboxMap
    private var currentMarker: PointAnnotation? = null
    private lateinit var pointAnnotationManager: PointAnnotationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_musician)

        val user = intent.getSerializableExtra("user") as User
        val createButton = findViewById<Button>(R.id.createButton)
        val backButton = findViewById<TextView>(R.id.backButton)
        val longitude = findViewById<TextView>(R.id.longitudeTextbox)
        val latitude = findViewById<TextView>(R.id.latitudeTextbox)
        val mapView = findViewById<MapView>(R.id.mapView)

        backButton.setOnClickListener(){
            finish()
        }

        createButton.setOnClickListener(){
            if(!(longitude.text.isEmpty() || latitude.text.isEmpty())){
                val musician = Musician(user.getId(), user.getIdRol(), user.getName(),
                    user.getEmail(), user.getPassword(), -1.toFloat(),
                    longitude.text.toString().toFloat(), latitude.text.toString().toFloat(),
                    "", ArrayList(), ArrayList(), ArrayList())
                setResult(Activity.RESULT_OK)

                createUser(user, musician)

                finish()
            }
        }

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
            mapboxMap = mapView.getMapboxMap()

            // Inicializar el manager de marcadores
            val annotationPlugin = mapView.annotations
            pointAnnotationManager = annotationPlugin.createPointAnnotationManager()

            // Obtener ubicación actual
            getUserLocation(mapView) { point ->
                addMarker(point, latitude, longitude)
            }

            // Detectar clicks en el mapa
            mapboxMap.addOnMapClickListener { clickedPoint ->
                val point = Point.fromLngLat(clickedPoint.longitude(), clickedPoint.latitude())
                updateMarker(point, latitude, longitude)
                true
            }
        }
    }

    private fun createUser(user: User, musician: Musician) {
        // Usar lifecycleScope para llamar a la función suspendida en el contexto adecuado
        lifecycleScope.launch {
            try {
                // Llamar a la función de creación de usuario
                val createdUser = ApiRepository.createUser(user)
                // Verificar si la respuesta fue exitosa
                if (createdUser != null) {
                    // Aquí puedes manejar la respuesta, como actualizar la UI con el nuevo usuario
                    Toast.makeText(this@NewMusicianActivity, "Usuario creado exitosamente: $createdUser", Toast.LENGTH_SHORT).show()
                    musician.setId(createdUser.getId())
                    ApiRepository.createMusician(musician)
                } else {
                    // Manejo de error si la creación del usuario falla
                    Toast.makeText(this@NewMusicianActivity, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Manejo de errores de red o excepciones
                Toast.makeText(this@NewMusicianActivity, "Error de conexión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserLocation(mapView: MapView, onLocationFound: (Point) -> Unit) {
        val locationComponentPlugin = mapView.location

        locationComponentPlugin.updateSettings {
            enabled = true
        }

        var ubicacionDetectada = false
        lateinit var listener: OnIndicatorPositionChangedListener

        listener = OnIndicatorPositionChangedListener { point ->
            if (!ubicacionDetectada) {
                ubicacionDetectada = true
                val userPoint = Point.fromLngLat(point.longitude(), point.latitude())
                onLocationFound(userPoint)
                mapboxMap.easeTo(CameraOptions.Builder().center(userPoint).zoom(14.0).build(),
                    mapAnimationOptions {
                    duration(1500) // duración en milisegundos
                })

                locationComponentPlugin.removeOnIndicatorPositionChangedListener(listener)
            }
        }

        locationComponentPlugin.addOnIndicatorPositionChangedListener(listener)

        mapView.postDelayed({
            if (!ubicacionDetectada) {
                ubicacionDetectada = true
                locationComponentPlugin.removeOnIndicatorPositionChangedListener(listener)
                val barcelonaPoint = Point.fromLngLat(2.1686, 41.3874)
                onLocationFound(barcelonaPoint)
                mapboxMap.easeTo(CameraOptions.Builder().center(barcelonaPoint).zoom(14.0).build(),
                    mapAnimationOptions {
                        duration(1500) // duración en milisegundos
                    })            }
        }, 1000)
    }

    private fun addMarker(point: Point, latitude: TextView, longitude: TextView) {
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)

        val drawable = getDrawable(R.drawable.musician_waypoint)
        val bitmap = drawableToBitmap(drawable!!, 100, 100)

        pointAnnotationManager.deleteAll()
        pointAnnotationManager.create(pointAnnotationOptions.apply {
            withIconImage(bitmap)
        }).also {
            currentMarker = it
        }

        latitude.text = point.latitude().toString()
        longitude.text = point.longitude().toString()
    }

    private fun updateMarker(point: Point, latitude: TextView, longitude: TextView) {
        currentMarker?.let { pointAnnotationManager.delete(it) }
        addMarker(point, latitude, longitude)
    }

    private fun drawableToBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
        val originalBitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(originalBitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return Bitmap.createScaledBitmap(originalBitmap, width, height, true)
    }
}