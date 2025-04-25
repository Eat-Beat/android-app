package com.example.eatbeat.activities.login

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eatbeat.R
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.User
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

class NewMusicianActivity : AppCompatActivity() {
    private lateinit var mapboxMap: MapboxMap
    private var currentMarker: PointAnnotation? = null
    private lateinit var pointAnnotationManager: PointAnnotationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_musician)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

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
            if(longitude.text.isEmpty() || latitude.text.isEmpty()){
                val musician = Musician(user.getId(), user.getIdRol(), user.getName(),
                    user.getEmail(), user.getPassword(), -1.toFloat(),
                    longitude.text.toString().toFloat(), latitude.text.toString().toFloat(),
                    "", ArrayList(), ArrayList(), ArrayList())
                setResult(Activity.RESULT_OK)
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