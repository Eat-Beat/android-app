package com.example.eatbeat.fragments

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician

class ContractDetailsFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_contract_details, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitwindow = view.findViewById<ImageView>(R.id.close_window)

        val dataBundle = arguments
        val contract = dataBundle?.getParcelable<Perform>("contract")
        val user = dataBundle?.getParcelable<Musician>("musician")



        exitwindow.setOnClickListener {
            val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_disappear)
            val fragmentManager = parentFragmentManager
            val fragment = fragmentManager.findFragmentById(R.id.contractDetailsScreen)

            val optionsScreen = requireActivity().findViewById<FragmentContainerView>(R.id.contractDetailsScreen)
            val opaqueBg = requireActivity().findViewById<View>(R.id.opaqueBg)
            optionsScreen.startAnimation(fadeOut)
            opaqueBg.startAnimation(fadeOut)
            opaqueBg.visibility = View.GONE
            closeAnimation(fadeOut, fragment)

        }


        setContractDetails(contract, user)
    }

    @SuppressLint("SetTextI18n")
    private fun setContractDetails(contract: Perform?, user: Musician?) {
        val musicName = view?.findViewById<TextView>(R.id.musicName)
        val musicRol = view?.findViewById<TextView>(R.id.musicRole)
        val musicStyle = view?.findViewById<TextView>(R.id.musicStyle)
        val musicPeriod = view?.findViewById<TextView>(R.id.musicPeriod)
        val musicCost = view?.findViewById<TextView>(R.id.musicCost)
        val dayLabel = view?.findViewById<TextView>(R.id.contractDayHeaderFragment)
        val placeName = view?.findViewById<TextView>(R.id.contractLocation)
        val locationImage = view?.findViewById<ImageView>(R.id.locationImage)!!

        musicName?.text = "MUSICIAN | " + user?.getName()
        musicRol?.text = "ROLE | " + user?.getClassification()?.get(0)
        musicStyle?.text = "STYLE | " + user?.getGenre()?.get(0)
        musicPeriod?.text = "PERIOD | " + contract?.getDate()
        musicCost?.text = "COST | " + contract?.getCost()
        dayLabel?.text =  contract?.getDate().toString()

        placeName?.text = user?.calculateLocationName(this.requireContext())


        val accessToken: String = getString(R.string.mapbox_access_token)
        val lon = user?.getLongitude()
        val lat = user?.getLatitude()

        val imageUrl : String = "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/"+
                lon + "," + lat + ",14,0/600x600?access_token=" + accessToken;

        Glide.with(this)
            .load(imageUrl)
            .into(locationImage);

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