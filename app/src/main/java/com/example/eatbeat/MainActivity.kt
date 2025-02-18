package com.example.eatbeat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contracts_calendar)

        val calendarView : CalendarView = findViewById(R.id.calendarView)
        calendarView.setCalendarDayLayout(R.layout.day_cell)
    }
}