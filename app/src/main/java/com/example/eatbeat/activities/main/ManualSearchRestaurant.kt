package com.example.eatbeat.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.ManualSearchAdapter
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson
import kotlinx.coroutines.launch

class ManualSearchRestaurant : AppCompatActivity() {
    private var restaurantList : ArrayList<Restaurant> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manual_search_restaurant)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)

        lifecycleScope.launch {
            try {
                val restaurants = getRestaurants()
                val restaurantsList = restaurants?.toMutableList() as ArrayList<Restaurant>
                restaurantList = restaurantsList
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        createList()
        activateSpinnerAlph()

        val search = findViewById<ImageView>(R.id.searchiconapply)
        search.setOnClickListener {
            updateListOnSearch()
        }

        val close = findViewById<ImageView>(R.id.close_modify_mr)
        close.setOnClickListener {
            val intent = Intent(this, SearchRestaurantActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    /**
     * Updates the list of restaurants based on the text submitted on the search box.
     */
    private fun updateListOnSearch() {
        val searchText = findViewById<EditText>(R.id.searchRestaurant)
        val filteredList = mutableListOf<Restaurant>()

        for (restaurant in restaurantList) {
            if (restaurant.getName().contains(searchText.text, ignoreCase = true)) {
                filteredList.add(restaurant)
            }
        }

        val restaurantRecycler = findViewById<RecyclerView>(R.id.recyclerManualSearch )

        restaurantRecycler.layoutManager = LinearLayoutManager(this)

        restaurantRecycler.adapter = ManualSearchAdapter(filteredList){ restaurant -> openProfile(restaurant)}
    }

    /**
     * Initates the spinner. Based on the option selected, it will sort the list of restaurants by alphabetic order.
     */
    private fun activateSpinnerAlph() {
        val spinnerAlphabetic = findViewById<Spinner>(R.id.spinneralph)
        val sortOptions = arrayOf(getString(R.string.asc), getString(R.string.desc))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAlphabetic.adapter = adapter

        spinnerAlphabetic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortRestaurants()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    /**
     * If the option selected is ascendent, it will sort the list. Moreover, if it is descendent, it will
     * also apply the reverse method to the list.
     */
    private fun sortRestaurants() {
        val spinnerAlphabetic = findViewById<Spinner>(R.id.spinneralph)
        val selectedSortOrder = spinnerAlphabetic.selectedItem.toString()

        restaurantList.sortBy { it.getName() }

        if (selectedSortOrder ==  getString(R.string.desc)) {
            restaurantList.reverse()
        }

        val restaurantRecycler = findViewById<RecyclerView>(R.id.recyclerManualSearch )
        restaurantRecycler.layoutManager = LinearLayoutManager(this)
        restaurantRecycler.adapter = ManualSearchAdapter(restaurantList){ restaurant -> openProfile(restaurant)}

    }

    /**
     * Creates the list for the activity.
     */
    private fun createList() {
        val restaurantRecycler = findViewById<RecyclerView>(R.id.recyclerManualSearch )

        restaurantRecycler.layoutManager = LinearLayoutManager(this)

        restaurantRecycler.adapter = ManualSearchAdapter(restaurantList){ restaurant -> openProfile(restaurant)}
    }

    /**
     * When clicking a restaurant, it will open its profile.
     */
    private fun openProfile(restaurant: Restaurant){
        val intent = Intent(this, ViewRestaurantActivity::class.java)
        intent.putExtra("restaurantId", restaurant.getId())
        startActivity(intent)
    }
}