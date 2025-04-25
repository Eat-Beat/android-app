package com.example.eatbeat.activities.contracts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.eatbeat.R
import com.example.eatbeat.activities.main.SearchMusicianActivity
import com.example.eatbeat.activities.main.SearchRestaurantActivity
import com.example.eatbeat.adapters.ContractsCalendarAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.Restaurant
import com.example.eatbeat.users.User
import com.example.eatbeat.utils.activateNavBar
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.example.eatbeat.utils.api.ApiRepository.getPerforms
import com.example.eatbeat.utils.api.ApiRepository.getPerformsByMusicianId
import com.example.eatbeat.utils.api.ApiRepository.getPerformsByRestaurantId
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.api.ApiRepository.getUsers
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class ContractsCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contracts_calendar)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val calendarView : CalendarView = findViewById(R.id.calendarView)
        calendarView.setCalendarDayLayout(R.layout.day_cell)

        val currentDayCalendar: Calendar = calendarView.currentPageDate
        val currDate : Date = currentDayCalendar.time

        activateNavBar(this, this, 2)


        lifecycleScope.launch {
            try {
                when(UserData.userType){
                    1 -> {
                        val contracts = getPerforms()!!
                        val musicians = getMusicians()!!
                        val restaurants = getRestaurants()!!
                        val filteredContracts = filterContracts(contracts)
                        highlightDaysWithContracts(calendarView, filteredContracts)
                        setCurrentDay(currDate, contracts, musicians, restaurants)
                        generateClickAndList(calendarView, contracts, musicians, restaurants)

                    }
                    2 -> {
                        val contracts = getPerforms()!!
                        val musicians = getMusicians()!!
                        val restaurants = getRestaurants()!!
                        val filteredContracts = filterContracts(contracts)
                        highlightDaysWithContracts(calendarView, filteredContracts)
                        setCurrentDay(currDate, contracts, musicians, restaurants)
                        generateClickAndList(calendarView, contracts, musicians, restaurants)

                    }
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }


        val seeListButton = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.buttonSeeList)

        seeListButton.setOnClickListener(){
            val intent = Intent(this, ContractListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val noeventsview = findViewById<ConstraintLayout>(R.id.noeventsview)
        noeventsview.visibility = View.VISIBLE
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

    private fun setCurrentDay(currDate: Date, contracts: List<Perform>, musicians: List<Musician>, restaurants : List<Restaurant>) {
        val contractOnDay: ArrayList<Perform> = ArrayList()
        val currId = UserData.userId

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        val currentDayText : TextView = findViewById(R.id.currentDaySelectedText)
        val formattedContractDate = simpleDateFormat.format(currDate)
        currentDayText.text = formattedContractDate

        for (contract in contracts) {
            var contractDate = contract.getDate()

            val calendarContractDate = Calendar.getInstance()
            calendarContractDate.time = contractDate
            calendarContractDate.set(Calendar.HOUR_OF_DAY, 0)
            calendarContractDate.set(Calendar.MINUTE, 0)
            calendarContractDate.set(Calendar.SECOND, 0)
            calendarContractDate.set(Calendar.MILLISECOND, 0)

            val formattedContractDate = simpleDateFormat.format(calendarContractDate.time)

            val formattedSelectedDate = simpleDateFormat.format(currDate)

            if (formattedContractDate == formattedSelectedDate && contract.getIdMusician() == currId) {
                contractOnDay.add(contract)
            }
        }

        val contractsCalendarRecycler = findViewById<RecyclerView>(R.id.contractsCalendarRecylcerView)

        contractsCalendarRecycler.layoutManager = LinearLayoutManager(this@ContractsCalendarActivity)

        val adapter = ContractsCalendarAdapter(contractOnDay, musicians, restaurants)
        contractsCalendarRecycler.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun generateClickAndList(calendarView : CalendarView, contracts : List<Perform>, musicians : List<Musician>, restaurants : List<Restaurant>) {
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val clickedDayCalendar: Calendar = calendarDay.calendar
                val date : Date = clickedDayCalendar.time

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")


                val currentDayText : TextView = findViewById(R.id.currentDaySelectedText)
                val formattedContractDate = simpleDateFormat.format(date)
                currentDayText.text = formattedContractDate

                loadContractsOnDay(date, contracts, musicians)
            }

            private fun loadContractsOnDay(date: Date, contracts : List<Perform>, musicians : List<Musician>) {
                val contractOnDay: ArrayList<Perform> = ArrayList()
                val currId = UserData.userId

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

                for (contract in contracts) {
                    var contractDate = contract.getDate()

                    val calendarContractDate = Calendar.getInstance()
                    calendarContractDate.time = contractDate
                    calendarContractDate.set(Calendar.HOUR_OF_DAY, 0)
                    calendarContractDate.set(Calendar.MINUTE, 0)
                    calendarContractDate.set(Calendar.SECOND, 0)
                    calendarContractDate.set(Calendar.MILLISECOND, 0)

                    val formattedContractDate = simpleDateFormat.format(calendarContractDate.time)

                    val formattedSelectedDate = simpleDateFormat.format(date)

                    if (formattedContractDate == formattedSelectedDate && contract.getIdMusician() == currId) {
                        contractOnDay.add(contract)
                    }
                }

                if (contractOnDay.isEmpty()){
                    val noeventsview = findViewById<ConstraintLayout>(R.id.noeventsview)
                    noeventsview.visibility = View.VISIBLE
                } else {
                    val noeventsview = findViewById<ConstraintLayout>(R.id.noeventsview)
                    noeventsview.visibility = View.GONE
                }

                val contractsCalendarRecycler = findViewById<RecyclerView>(R.id.contractsCalendarRecylcerView)

                contractsCalendarRecycler.layoutManager = LinearLayoutManager(this@ContractsCalendarActivity)

                val adapter = ContractsCalendarAdapter(contractOnDay, musicians, restaurants)
                contractsCalendarRecycler.adapter = adapter

                adapter.notifyDataSetChanged()

            }

            private fun isSameDay(selectedDayDate: Date, contractDate: Date): Boolean {
                val calendar1 = Calendar.getInstance()
                calendar1.time = selectedDayDate

                val calendar2 = Calendar.getInstance()
                calendar2.time = contractDate

                return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                        calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                        calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
            }

        })
    }

    private fun highlightDaysWithContracts(calendarView: CalendarView, contracts: List<Perform>) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val highlightedDays = mutableListOf<CalendarDay>()

        for (contract in contracts) {
            val contractDate = contract.getDate()
            val calendarContractDate = Calendar.getInstance()
            calendarContractDate.time = contractDate
            calendarContractDate.set(Calendar.HOUR_OF_DAY, 0)
            calendarContractDate.set(Calendar.MINUTE, 0)
            calendarContractDate.set(Calendar.SECOND, 0)
            calendarContractDate.set(Calendar.MILLISECOND, 0)

            val calendarDay = CalendarDay(calendarContractDate)
            calendarDay.labelColor =  R.color.orange
            highlightedDays.add(calendarDay)
        }

        calendarView.setCalendarDays(highlightedDays)
    }
}