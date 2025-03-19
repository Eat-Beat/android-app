package com.example.eatbeat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.eatbeat.adapters.ContractsCalendarAdapter
import com.example.eatbeat.adapters.ContractsListAdapter
import com.example.eatbeat.adapters.MusicianAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class ContractsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contracts_calendar)

        overridePendingTransition(R.anim.transition_fade_activity, 0)

        val navContractIc = findViewById<ImageView>(R.id.navCalendarIcon)
        navContractIc.setImageResource(R.drawable.contracts_selected)

        val calendarView : CalendarView = findViewById(R.id.calendarView)
        calendarView.setCalendarDayLayout(R.layout.day_cell)

        val currentDayCalendar: Calendar = calendarView.currentPageDate
        val currDate : Date = currentDayCalendar.time

        activateNavBar()

        setCurrentDay(currDate, loadContractsFromJson(loadJsonFromRaw(this, R.raw.contracts)!!), loadMusiciansFromJson(loadJsonFromRaw(this, R.raw.musicians)!!))

        generateClickAndList(calendarView, loadContractsFromJson(loadJsonFromRaw(this, R.raw.contracts)!!), loadMusiciansFromJson(loadJsonFromRaw(this, R.raw.musicians)!!))

        val seeListButton = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.buttonSeeList)

        seeListButton.setOnClickListener(){
            val intent = Intent(this, ContractListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setCurrentDay(currDate: Date, contracts: ArrayList<Perform>, musicians: ArrayList<Musician>) {
        val contractOnDay: ArrayList<Perform> = ArrayList()

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

            if (formattedContractDate == formattedSelectedDate) {
                contractOnDay.add(contract)
            }
        }

        val contractsCalendarRecycler = findViewById<RecyclerView>(R.id.contractsCalendarRecylcerView)

        contractsCalendarRecycler.layoutManager = LinearLayoutManager(this@ContractsActivity)

        val adapter = ContractsCalendarAdapter(contractOnDay, musicians)
        contractsCalendarRecycler.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun activateNavBar(){
        val navSearch = findViewById<ImageView>(R.id.navMusicianIcon)
        val navChat = findViewById<ImageView>(R.id.navChatIcon)
        val navProfile = findViewById<ImageView>(R.id.navProfileIcon)

        navSearch.setOnClickListener(){
            val intent = Intent(this, SearchMusicianActivity::class.java)
            startActivity(intent)
            finish()
        }

        navChat.setOnClickListener(){
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        navProfile.setOnClickListener(){
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun generateClickAndList(calendarView : CalendarView, contracts : ArrayList<Perform>, musicians : ArrayList<Musician>) {
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

            private fun loadContractsOnDay(date: Date, contracts : ArrayList<Perform>, musicians : ArrayList<Musician>) {
                val contractOnDay: ArrayList<Perform> = ArrayList()

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

                    if (formattedContractDate == formattedSelectedDate) {
                        contractOnDay.add(contract)
                    }
                }

                val contractsCalendarRecycler = findViewById<RecyclerView>(R.id.contractsCalendarRecylcerView)

                contractsCalendarRecycler.layoutManager = LinearLayoutManager(this@ContractsActivity)

                val adapter = ContractsCalendarAdapter(contractOnDay, musicians)
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
}