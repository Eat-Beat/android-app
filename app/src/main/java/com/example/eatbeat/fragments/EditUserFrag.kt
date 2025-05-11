package com.example.eatbeat.fragments

import android.R.attr.path
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.musicianAttributes.Multimedia
import com.example.eatbeat.utils.api.ApiRepository.getMusicianById
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.api.ApiRepository.updateMultimedia
import com.example.eatbeat.utils.uploadBitmap
import kotlinx.coroutines.launch
import java.io.File


class EditUserFrag : Fragment() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null
    private var buttonProfileImageEdit: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_modify_user, container, false)
        val exitStats = view.findViewById<ImageView>(R.id.close_modify_mr)

        buttonProfileImageEdit = view.findViewById(R.id.profileImageEdit)

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                buttonProfileImageEdit?.setImageURI(photoUri)
            }
        }

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

        val save = view.findViewById<ImageView>(R.id.saveChanges)
        save.setOnClickListener {
            saveChanges()
        }

        buttonProfileImageEdit?.setOnClickListener {
            val imageFile = File.createTempFile("photo_", ".jpg", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", imageFile)
            photoUri = uri
            takePictureLauncher.launch(uri)
        }

        buttonProfileImageEdit?.setOnLongClickListener {
            getPhotoFromGallery()
        }

        return view
    }

    private fun saveChanges(){
        val profileImage = view?.findViewById<ImageView>(R.id.profileImageEdit)

    }

    /**
     * Initates photoPicker to get an image from the gallery
     */
    private fun getPhotoFromGallery(): Boolean {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 1)
        return true
    }

    /**
     * When ending gallery photoPicker, get the image and loads it into the view,
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val selectedImageUri = data?.data

            selectedImageUri?.let {
                Glide.with(this).load(it).into(buttonProfileImageEdit!!)
            }
        }
    }

    /**
     * Closing animation when exiting the fragment.
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