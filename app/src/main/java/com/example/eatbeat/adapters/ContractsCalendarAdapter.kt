package com.example.eatbeat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician

class ContractsCalendarAdapter(private val contracts: List<Perform>, private val musicians: List<Musician>) :
    RecyclerView.Adapter<ContractsCalendarAdapter.CalendarListHolder>() {

    class CalendarListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textCell: TextView = view.findViewById(R.id.contractDetailsCellCalendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contract_calendar_list_cell, parent, false)
        return CalendarListHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CalendarListHolder, position: Int) {
        val contract = contracts[position]
        val musician = musicians.find { it.getId() == contract.getIdMusician() }

        holder.textCell.text = musician?.getName() + " · " + musician?.getClassification()!![0] + " · " +
                musician.getGenre()[0] + " · " + contract.getDate()
    }

    override fun getItemCount() = contracts.size
}
