package com.example.eatbeat.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.ContractsListAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.fragments.ContractDetailsFrag
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson

class ContractListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contracts_list)
        val navContractIc = findViewById<ImageView>(R.id.navCalendarIcon)
        navContractIc.setImageResource(R.drawable.contracts_selected)

        activateNavBar(this,this, 2)

        val seeCalendarButton = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.seeCalendarButton)

        seeCalendarButton.setOnClickListener(){
            val intent = Intent(this, ContractsActivity::class.java)
            startActivity(intent)
            finish()
        }

        fillList(loadContractsFromJson(loadJsonFromRaw(this, R.raw.contracts)!!), loadMusiciansFromJson(loadJsonFromRaw(this,
            R.raw.musicians
        )!!))
    }

    private fun fillList(contracts : ArrayList<Perform>, musicians : ArrayList<Musician>) {
        val contractsListRecycler = findViewById<RecyclerView>(R.id.contractListRecyclerView)

        contractsListRecycler.layoutManager = LinearLayoutManager(this)

        val adapter = ContractsListAdapter(contracts, musicians) { contract, musician ->
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