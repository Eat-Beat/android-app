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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.ManualSearchAdapter
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson

class ManualSearchRestaurant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manual_search_restaurant)


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

    private fun updateListOnSearch() {
        val searchText = findViewById<EditText>(R.id.searchRestaurant)
        val restaurantList = loadRestaurantsFromJson(loadJsonFromRaw(this, R.raw.restaurans)!!)
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

    private fun activateSpinnerAlph() {
        val spinnerAlphabetic = findViewById<Spinner>(R.id.spinneralph)
        val sortOptions = arrayOf("Ascendente", "Descendente")
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

    private fun sortRestaurants() {
        val spinnerAlphabetic = findViewById<Spinner>(R.id.spinneralph)
        val selectedSortOrder = spinnerAlphabetic.selectedItem.toString()
        val restaurantList = loadRestaurantsFromJson(loadJsonFromRaw(this, R.raw.restaurans)!!)

        restaurantList.sortBy { it.getName() }

        if (selectedSortOrder == "Descendente") {
            restaurantList.reverse()
        }

        val restaurantRecycler = findViewById<RecyclerView>(R.id.recyclerManualSearch )
        restaurantRecycler.layoutManager = LinearLayoutManager(this)
        restaurantRecycler.adapter = ManualSearchAdapter(restaurantList){ restaurant -> openProfile(restaurant)}

    }

    private fun createList() {
        val restaurantList = loadRestaurantsFromJson(loadJsonFromRaw(this, R.raw.restaurans)!!)
        val restaurantRecycler = findViewById<RecyclerView>(R.id.recyclerManualSearch )

        restaurantRecycler.layoutManager = LinearLayoutManager(this)

        restaurantRecycler.adapter = ManualSearchAdapter(restaurantList){ restaurant -> openProfile(restaurant)}
    }

    private fun openProfile(restaurant: Restaurant){
        val intent = Intent(this, ViewRestaurantActivity::class.java)
        intent.putExtra("restaurantId", restaurant.getId())
        startActivity(intent)
    }
}