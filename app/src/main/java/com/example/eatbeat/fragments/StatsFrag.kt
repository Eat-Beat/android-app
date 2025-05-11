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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatbeat.R
import com.example.eatbeat.adapters.RestaurantReviewAdapter
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.data.UserData
import com.example.eatbeat.users.Musician
import com.example.eatbeat.users.User
import com.example.eatbeat.utils.api.ApiRepository.getMusicianById
import com.example.eatbeat.utils.api.ApiRepository.getMusicians
import com.example.eatbeat.utils.api.ApiRepository.getPerforms
import com.example.eatbeat.utils.api.ApiRepository.getPerformsByMusicianId
import com.example.eatbeat.utils.api.ApiRepository.getRestaurants
import com.example.eatbeat.utils.loadContractsForProfileFromJson
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
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
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatsFrag : Fragment() {

    private lateinit var contractsMusician : List<Perform>

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


        getContractsFromMusician()
        createEventsLineChart(contractsMusician)
        createReviewsChart()
        fillInReviews()

    }

    /**
     * Closing animation when exiting fragment,
     */
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

    /**
     * Creates the event line chart. Filters contracts based on month, and
     * counts the number of contracts per month. Then applies them to the chart.
     */
    private fun createEventsLineChart(contracts: List<Perform>) {
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

    /**
     * Gets the number of performances per month. Returns a list of pairs with the month and the number of performances.
     */
    private fun getMonthlyPerformances(contracts: List<Perform>): ArrayList<Pair<String, Int>> {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val monthlyCounts = mutableMapOf<String, Int>()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        for (month in 0..11) {
            val monthFormatted = String.format("%04d-%02d", year, month + 1)
            monthlyCounts[monthFormatted] = 0
        }

        for (perform in contracts) {
            val date = perform.getDate()
            val monthYear = sdf.format(date)
            monthlyCounts[monthYear] = monthlyCounts.getOrDefault(monthYear, 0) + 1
        }

        return ArrayList(monthlyCounts.toList().sortedBy { it.first })
    }

    /**
     * Creates a bar chart with the reviews of the musician.
     */
    private fun createReviewsChart() {
        val reviewsList = getReviewsArray()
        val totalReviews = reviewsList.sum()

        val percentages = if (totalReviews > 0) {
            reviewsList.map { (it.toFloat() / totalReviews) * 100 }
        } else {
            List(5) { 0f }
        }

        val barChart = view?.findViewById<BarChart>(R.id.reviewsChart)!!

        val entries = ArrayList<BarEntry>()
        for (i in percentages.indices) {
            entries.add(BarEntry(i.toFloat(), percentages[i]))
        }

        val dataSet = BarDataSet(entries, "Reviews").apply {
            color = ContextCompat.getColor(requireContext(), R.color.orange)
            setDrawValues(false)
            valueTextSize = 12f
        }

        val barData = BarData(dataSet)
        barData.barWidth = 0.8f

        barChart.apply {
            data = barData
            setDrawValueAboveBar(false)
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            legend.isEnabled = false
            animateY(1000)

            xAxis.apply {
                isEnabled = true
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                axisMinimum = -0.5f
                axisMaximum = 4.5f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${(value.toInt() + 1)}★"
                    }
                }
            }

            axisLeft.apply {
                isEnabled = true
                setDrawGridLines(false)
                setDrawLabels(false)
                granularity = 1f
                axisMinimum = -0.5f
                axisMaximum = 100f
            }

            axisRight.apply {
                isEnabled = false
            }

            invalidate()
        }
    }

    /**
     * Filters reviews based on the number of stars. Then returns an array with the number of reviews per star.
     */
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

    /**
     * Gets the number of contracts from the musician.
     */
    private fun getContractsFromMusician(){
        val contractsFromMusician: MutableList<Perform> = mutableListOf()

        val reviews: ArrayList<Perform> = loadContractsFromJson(loadJsonFromRaw(requireContext(), R.raw.contracts)!!)
        val musicians: ArrayList<Musician> = loadMusiciansFromJson(loadJsonFromRaw(requireContext(), R.raw.musicians)!!)
        val musician = musicians.find { it.getId() == UserData.userId }!!

        musician.let { m ->
            contractsFromMusician.addAll(reviews.filter { it.getIdMusician() == m.getId() })
        }

        contractsMusician = contractsFromMusician

    }

    /**
     * Fills the reviews the musician has received.
     */
    private fun fillInReviews(){
        val reviewsRecyclerView : RecyclerView? = view?.findViewById(R.id.reviewsList)

        reviewsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            try {
                val contracts = getPerformsByMusicianId(UserData.userId)!!
                val restaurants = getRestaurants()!!
                reviewsRecyclerView?.adapter = RestaurantReviewAdapter(contracts, restaurants)

            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

    }
}