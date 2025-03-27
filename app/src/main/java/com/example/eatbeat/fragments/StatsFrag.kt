package com.example.eatbeat.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.RestaurantReviewAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.utils.loadContractsForProfileFromJson
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadRestaurantsFromJson
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatsFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitStats = view.findViewById<ImageView>(R.id.close_stats)

        exitStats.setOnClickListener {
            val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_disappear)
            val fragmentManager = parentFragmentManager
            val fragment = fragmentManager.findFragmentById(R.id.ratingsScreen)

            val statsScreen = requireActivity().findViewById<FragmentContainerView>(R.id.ratingsScreen)
            val opaqueBg = requireActivity().findViewById<View>(R.id.opaqueBg)
            statsScreen.startAnimation(fadeOut)
            opaqueBg.startAnimation(fadeOut)
            opaqueBg.visibility = View.GONE
            closeAnimation(fadeOut, fragment)

        }


        createEventsLineChart(loadContractsFromJson(loadJsonFromRaw(requireContext(), R.raw.contracts)!!))
        createReviewsChart()
        fillInReviews()

    }

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

    private fun createEventsLineChart(contracts: ArrayList<Perform>) {
        val actsCount = getMonthlyPerformances(contracts)

        val entries = ArrayList<Entry>()
        for (i in actsCount.indices) {
            entries.add(Entry(i.toFloat(), actsCount[i].second.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "Actuaciones por mes").apply {
            color = Color.parseColor("#000000")
            valueTextColor = Color.TRANSPARENT //
            lineWidth = 2f
            setDrawCircles(false)
            setDrawValues(false)
            setMode(LineDataSet.Mode.CUBIC_BEZIER)
        }

        val lineData = LineData(lineDataSet)

        val linechart = view?.findViewById<LineChart>(R.id.actsChart)

        linechart?.apply {
            data = lineData
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            legend.isEnabled = true
            legend.textColor = Color.GRAY
            legend.formSize = 10f

            axisLeft.apply {
                setDrawLabels(true)
                textColor = Color.GRAY
                setDrawGridLines(true)
                granularity = 5f
                axisMinimum = 0f
                axisMaximum = 30f
                setLabelCount(6, true)
            }

            axisRight.isEnabled = false

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"))
                granularity = 1f
                labelCount = 12
                textColor = Color.GRAY
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                isGranularityEnabled = true //
            }

            invalidate()
        }
    }

    private fun getMonthlyPerformances(contracts: ArrayList<Perform>): ArrayList<Pair<String, Int>> {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val monthlyCounts = mutableMapOf<String, Int>()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        for (month in 0..11) {
            val monthFormatted = String.format("%04d-%02d", year, month + 1)
            monthlyCounts[monthFormatted] = 0
        }

        for (perform in contracts) {
            val date = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.getDefault()).parse(perform.dateTime.toString())
            val monthYear = sdf.format(date!!)
            monthlyCounts[monthYear] = monthlyCounts.getOrDefault(monthYear, 0) + 1
        }

        return ArrayList(monthlyCounts.toList().sortedBy { it.first })
    }

    private fun createReviewsChart(){
        val reviewsList = getReviewsArray()

        val totalReviews = reviewsList.sum()

        val percentages = ArrayList<Float>()
        if (totalReviews > 0) {
            for (count in reviewsList) {
                val percentage = (count.toFloat() / totalReviews) * 100
                percentages.add(percentage)
            }
        } else {
            percentages.addAll(List(5) { 0f })
        }

        val barChart = view?.findViewById<BarChart>(R.id.reviewsChart)!!

        val entries = ArrayList<BarEntry>()
        for (i in percentages.indices) {
            entries.add(BarEntry(i.toFloat(), percentages[i]))
        }

        val dataSet = BarDataSet(entries, "Reviews").apply {
            color = ContextCompat.getColor(requireContext(), R.color.orange)
        }

        dataSet.setDrawValues(false)

        val barData = BarData(dataSet)

        barChart.apply {
            data = barData
            setDrawValueAboveBar(true)
            setBackgroundColor(Color.WHITE)
            xAxis.isEnabled = false
            axisLeft.isEnabled = true
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            animateY(1000)

            axisLeft.apply {
                setDrawLabels(true)
                setDrawGridLines(true)
                granularity = 1f
                axisMinimum = 0f
                axisMaximum = 5f
                setLabelCount(6, true)
                legend.isEnabled = false
            }


            invalidate()
        }
    }

    private fun getReviewsArray() : ArrayList<Int> {
        val array = ArrayList<Int>(5).apply {
            for (i in 0 until 5) add(0)
        }

        val reviews: ArrayList<Perform> = loadContractsFromJson(loadJsonFromRaw(requireContext(), R.raw.contracts)!!)

        for (review in reviews) {
            val rate = review.restaurantRate
            if (rate in 1..5) {
                array[rate - 1] += 1
            }
        }

        return array
    }

    private fun fillInReviews(){
        val reviewsRecyclerView : RecyclerView? = view?.findViewById(R.id.reviewsList)

        reviewsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        reviewsRecyclerView?.adapter = RestaurantReviewAdapter(loadContractsForProfileFromJson(loadJsonFromRaw(requireContext(), R.raw.contract_3)!!),
                                                               loadRestaurantsFromJson(loadJsonFromRaw(requireContext(), R.raw.restaurans)!!)
                                                              )
    }
}