package com.example.eatbeat.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R


class EditUserFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_modify_user, container, false)

        val exitStats = view.findViewById<ImageView>(R.id.close_modify_mr)

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

        saveChanges()
        getPhotoFromGallery()

        return view
    }

    private fun saveChanges(){
        val profileImage = view?.findViewById<ImageView>(R.id.profileImageEdit)
        val name = view?.findViewById<EditText>(R.id.nameEdit)
        val description = view?.findViewById<EditText>(R.id.descriptionEdit)
        val multimedia = view?.findViewById<RecyclerView>(R.id.multimediaListEdit)

    }

    private fun getPhotoFromGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.setType("image/*")
        startActivityForResult(photoPickerIntent, R.id.profileImageEdit)
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