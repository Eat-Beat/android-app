package com.example.eatbeat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.eatbeat.adapters.ContractsCalendarAdapter
import com.example.eatbeat.adapters.ContractsListAdapter
import com.example.eatbeat.adapters.MusicianAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
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

        activateNavBar()

        generateClickAndList(calendarView)

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

    private fun generateClickAndList(calendarView : CalendarView) {
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val clickedDayCalendar: Calendar = calendarDay.calendar
                val date : Date = clickedDayCalendar.time

                loadContractsOnDay(date)
            }

            private fun loadContractsOnDay(date: Date, contracts : ArrayList<Perform>, musicians : ArrayList<Musician>) {
                val contractOnDay: ArrayList<Perform> = ArrayList()

                for (contract in contracts) {
                    val contractDate = contract.getDate()
                    if (isSameDay(date, contractDate)) {
                        contractOnDay.add(contract)
                    }
                }

                val contractsCalendarRecycler = findViewById<RecyclerView>(R.id.contractsCalendarRecylcerView)

                contractsCalendarRecycler.adapter = ContractsCalendarAdapter(contracts, musicians)

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