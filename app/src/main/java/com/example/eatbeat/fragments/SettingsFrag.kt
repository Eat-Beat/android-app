package com.example.eatbeat.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.eatbeat.R
import com.example.eatbeat.activities.login.LoginActivity

class SettingsFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_options_menu, container, false)
        val exitwindow = view.findViewById<ImageView>(R.id.close_window_view)
        val closeSesion = view.findViewById<TextView>(R.id.closeSesionButton)

        exitwindow.setOnClickListener {
            val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_disappear)
            val fragmentManager = parentFragmentManager
            val fragment = fragmentManager.findFragmentById(R.id.optionsScreen)

            val optionsScreen = requireActivity().findViewById<FragmentContainerView>(R.id.optionsScreen)
            val opaqueBg = requireActivity().findViewById<View>(R.id.opaqueBg)
            optionsScreen.startAnimation(fadeOut)
            opaqueBg.startAnimation(fadeOut)
            opaqueBg.visibility = View.GONE
            closeAnimation(fadeOut, fragment)


        }

        closeSesion.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

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