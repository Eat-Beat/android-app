package com.example.eatbeat.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.eatbeat.R
import com.example.eatbeat.contracts.Perform
import com.example.eatbeat.users.Musician
import com.example.eatbeat.utils.loadContractsFromJson
import com.example.eatbeat.utils.loadJsonFromRaw
import com.example.eatbeat.utils.loadMusiciansFromJson
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Map

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
            // Asegurar que los valores de X están correctamente distribuidos
            entries.add(Entry(i.toFloat(), actsCount[i].second.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "Actuaciones por mes").apply {
            color = Color.parseColor("#000000") // Amarillo pastel
            valueTextColor = Color.TRANSPARENT // Ocultar valores sobre la línea
            lineWidth = 2f
            setDrawCircles(false) // Ocultar puntos
            setDrawValues(false) // No mostrar valores sobre la línea
            setMode(LineDataSet.Mode.CUBIC_BEZIER) // Líneas curvas
        }

        val lineData = LineData(lineDataSet)

        val linechart = view?.findViewById<LineChart>(R.id.actsChart)

        linechart?.apply {
            data = lineData
            setBackgroundColor(Color.WHITE) // Fondo blanco
            description.isEnabled = false
            legend.isEnabled = true
            legend.textColor = Color.GRAY
            legend.formSize = 10f

            // Configurar eje Y (valores en múltiplos de 5)
            axisLeft.apply {
                setDrawLabels(true)
                textColor = Color.GRAY
                setDrawGridLines(true)
                granularity = 5f // Intervalos de 5 en 5
                axisMinimum = 0f
                axisMaximum = 30f // Ajustar según el rango esperado
                setLabelCount(6, true) // Forzar que se muestren varios valores
            }

            axisRight.isEnabled = false // Ocultar eje derecho

            // Configurar eje X (Mostrar 12 meses correctamente distribuidos)
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(
                    listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
                                                        ) // Mostrar nombres de los meses
                granularity = 1f // Asegurar que muestre cada mes
                labelCount = 12 // Forzar que se muestren los 12 meses
                textColor = Color.GRAY
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                isGranularityEnabled = true // Evitar etiquetas repetidas
            }

            invalidate() // Refrescar gráfico
        }
    }


    private fun getMonthlyPerformances(contracts: ArrayList<Perform>): ArrayList<Pair<String, Int>> {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val monthlyCounts = mutableMapOf<String, Int>()

        // Inicializar todos los meses con 0
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        for (month in 0..11) {
            val monthFormatted = String.format("%04d-%02d", year, month + 1)
            monthlyCounts[monthFormatted] = 0
        }

        // Contar las actuaciones por mes
        for (perform in contracts) {
            val date = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.getDefault()).parse(perform.dateTime.toString())
            val monthYear = sdf.format(date!!)
            monthlyCounts[monthYear] = monthlyCounts.getOrDefault(monthYear, 0) + 1
        }

        // Convertir a ArrayList y ordenar los meses
        return ArrayList(monthlyCounts.toList().sortedBy { it.first })
    }

    private fun createReviewsChart(){

    }

    private fun fillInReviews(){

    }
}