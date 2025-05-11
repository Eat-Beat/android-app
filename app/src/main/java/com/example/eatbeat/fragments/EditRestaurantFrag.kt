package com.example.eatbeat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.eatbeat.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

class EditRestaurantFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_restaurant, container, false)

        val exitStats = view.findViewById<ImageView>(R.id.close_modify_mr)

        val mapView = view.findViewById<MapView>(R.id.mapRestaurantEdit)

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(2.17328, 41.38868))
                .zoom(14.0)
                .build()
        )

        exitStats.setOnClickListener {
            val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_disappear)
            val fragmentManager = parentFragmentManager
            val fragment = fragmentManager.findFragmentById(R.id.editUserScreen)

            val editScreen = requireActivity().findViewById<FragmentContainerView>(R.id.editUserScreen)
            val opaqueBg = requireActivity().findViewById<View>(R.id.opaqueBg)
            editScreen.startAnimation(fadeOut)
            opaqueBg.startAnimation(fadeOut)
            opaqueBg.visibility = View.GONE

            closeAnimation(fadeOut, fragment)
        }

        return view
    }

    /**
     * Closing animation when exiting fragment.
     */
    private fun closeAnimation(fadeOut : Animation, fragment : Fragment?){
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                if (fragment != null) {
                    fragmentManager?.beginTransaction()
                        ?.hide(fragment)
                        ?.commit()
                }

            }

            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}