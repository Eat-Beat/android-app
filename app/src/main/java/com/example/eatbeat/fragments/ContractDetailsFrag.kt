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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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

    /**
     * Load the contract info.
     */
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
        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
        val dateFormat2 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())


        musicName?.text = getString(R.string.musician)+ " | " + user?.getName()
        musicRol?.text = getString(R.string.rol)+ " | " + user?.getClassification()?.get(0)
        musicStyle?.text = getString(R.string.style)+ " | " + user?.getGenre()?.get(0)
        musicPeriod?.text = getString(R.string.period)+ " | " + contract?.getDate().let { dateFormat2.format(it!!) }
        musicCost?.text = getString(R.string.cost)+ " | " + contract?.getCost()
        dayLabel?.text =  contract?.getDate().let { dateFormat.format(it!!) }

        placeName?.text = user?.calculateLocationName(this.requireContext())


        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()!!
                val userId = UserData.userId

                val restaurant = restaurants.find { it.getId() == userId }!!

                val multimedia = restaurant.getMultimedia()
                if (multimedia == null || multimedia.getImage() == null) {
                    Glide.with(requireContext())
                        .load(R.drawable.not_load_restaurant_bc)
                        .into(locationImage)
                } else {
                    Glide.with(requireContext())
                        .load(multimedia.getImage())
                        .into(locationImage)
                }

            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }



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