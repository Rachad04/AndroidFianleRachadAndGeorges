package com.usj.midtermrachadsouaiby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.usj.midtermrachadsouaiby.data.Spending

class SpendingAnalysisActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_analysis)

        // Initialize Charts
        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)

        // Dummy data
        val spendings = listOf(
            Spending(1, "Grocery shopping", 50.0, "2025-01-06"),
            Spending(2, "Movie tickets", 120.0, "2025-01-05"),
            Spending(3, "Dining out", 80.0, "2025-01-04"),
            Spending(4, "Transportation", 30.0, "2025-01-03")
        )

        // Setup and display charts
        setupPieChart(spendings)
        setupBarChart(spendings)
    }

    private fun setupPieChart(spendings: List<Spending>) {
        val pieEntries = spendings.map { PieEntry(it.amount.toFloat(), it.description) }
        val pieDataSet = PieDataSet(pieEntries, "Spending Categories")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false // Disable description
        pieChart.invalidate() // Refresh
    }

    private fun setupBarChart(spendings: List<Spending>) {
        val barEntries = spendings.mapIndexed { index, spending ->
            BarEntry(index.toFloat(), spending.amount.toFloat())
        }
        val barDataSet = BarDataSet(barEntries, "Spending Over Time")
        barDataSet.color = ContextCompat.getColor(this, android.R.color.holo_blue_dark)

        val barData = BarData(barDataSet)
        barChart.data = barData

        // Customize Bar Chart
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false) // Remove gridlines
        xAxis.setDrawAxisLine(true) // Keep only bottom X-axis

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(false) // Remove left gridlines

        barChart.axisRight.isEnabled = false // Remove right axis
        barChart.description.isEnabled = false // Disable description
        barChart.invalidate() // Refresh
    }
}
