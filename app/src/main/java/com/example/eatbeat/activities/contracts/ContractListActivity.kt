package com.example.eatbeat.activities.contracts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.ContractsListAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.data.UserData
import com.example.eatbeat.fragments.ContractDetailsFrag
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.example.eatbeat.utils.api.ApiRepository.getPerforms
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
import kotlinx.coroutines.launch

class ContractListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contracts_list)

        activateNavBar(this,this, 2)

        val seeCalendarButton = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.seeCalendarButton)

        seeCalendarButton.setOnClickListener(){
            val intent = Intent(this, ContractsCalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        lifecycleScope.launch {
            try {
                when(UserData.userType){
                    1 -> {
                        val contracts = getPerforms()!!
                        val musicians = getMusicians()!!
                        val restaurants = getRestaurants()!!
                        val filteredContracts = filterContracts(contracts)
                        fillList(filteredContracts, musicians, restaurants)
                    }
                    2 -> {
                        val contracts = getPerforms()!!
                        val musicians = getMusicians()!!
                        val restaurants = getRestaurants()!!
                        val filteredContracts = filterContracts(contracts)
                        fillList(filteredContracts, musicians, restaurants)
                    }
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

    }

    private fun filterContracts(contractsAPI : List<Perform>) : List<Perform> {
        val filteredContracts: MutableList<Perform> = mutableListOf()
        val contracts = contractsAPI
        val currId = UserData.userId

        when(UserData.userType){
            1 -> {
                filteredContracts.addAll(contracts.filter { it.getIdMusician() == currId })
            }
            2 -> {
                filteredContracts.addAll(contracts.filter { it.getIdRestaurant() == currId })
            }
        }

        return filteredContracts
    }

    private fun fillList(contracts : List<Perform>, musicians : List<Musician>, restaurants : List<Restaurant>) {
        val contractsListRecycler = findViewById<RecyclerView>(R.id.contractListRecyclerView)

        contractsListRecycler.layoutManager = LinearLayoutManager(this)

        val adapter = ContractsListAdapter(contracts, musicians, restaurants) { contract, musician ->
            val fragment = ContractDetailsFrag()

            val bundle = Bundle().apply {
                putParcelable("contract", contract)
                putParcelable("musician", musician)
            }

            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.contractDetailsScreen, fragment)
                .addToBackStack(null)
                .commit()

            val fragmentContractDetails = findViewById<FragmentContainerView>(R.id.contractDetailsScreen)
            fragmentContractDetails.visibility = View.VISIBLE

            val opaqueBg = findViewById<View>(R.id.opaqueBg)

            opaqueBg.visibility = View.VISIBLE
        }

        contractsListRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}