package com.usj.budgettracking

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.usj.budgettracking.database.AppDatabaseHelper

class SpendingAnalysisFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spending_analysis, container, false)

        // Initialize Views
        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)
        recyclerView = view.findViewById(R.id.recyclerViewTransactions)
        spinner = view.findViewById(R.id.spinnerAnalysisType)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.analysis_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Database Helper
        val dbHelper = AppDatabaseHelper(requireContext())

        // Spinner Selection Listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setupAnalysisByCategories(dbHelper)
                    1 -> setupAnalysisByDate(dbHelper)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupAnalysisByCategories(dbHelper: AppDatabaseHelper) {
        val groupedData = fetchGroupedByCategory(dbHelper)

        val pieEntries = groupedData.map { PieEntry(it.value.toFloat(), it.key) }
        val barEntries = groupedData.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        updateCharts(pieEntries, barEntries)
    }

    private fun setupAnalysisByDate(dbHelper: AppDatabaseHelper) {
        val groupedData = fetchGroupedByDate(dbHelper)

        val pieEntries = groupedData.map { PieEntry(it.value.toFloat(), it.key) }
        val barEntries = groupedData.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        updateCharts(pieEntries, barEntries)
    }

    private fun updateCharts(pieEntries: List<PieEntry>, barEntries: List<BarEntry>) {
        // Update PieChart
        val pieDataSet = PieDataSet(pieEntries, "Analysis")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        pieChart.data = PieData(pieDataSet)
        pieChart.invalidate()

        // Update BarChart
        val barDataSet = BarDataSet(barEntries, "Analysis")
        barDataSet.color = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark)
        barChart.data = BarData(barDataSet)
        barChart.invalidate()
    }

    private fun fetchGroupedByCategory(dbHelper: AppDatabaseHelper): Map<String, Double> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT ${AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY}, SUM(${AppDatabaseHelper.COLUMN_EXPENSE_AMOUNT}) as total " +
                    "FROM ${AppDatabaseHelper.TABLE_EXPENSES} GROUP BY ${AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY}",
            null
        )

        val groupedData = mutableMapOf<String, Double>()
        while (cursor.moveToNext()) {
            val category = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
            groupedData[category] = total
        }
        cursor.close()
        return groupedData
    }

    private fun fetchGroupedByDate(dbHelper: AppDatabaseHelper): Map<String, Double> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT ${AppDatabaseHelper.COLUMN_EXPENSE_DATE}, SUM(${AppDatabaseHelper.COLUMN_EXPENSE_AMOUNT}) as total " +
                    "FROM ${AppDatabaseHelper.TABLE_EXPENSES} GROUP BY ${AppDatabaseHelper.COLUMN_EXPENSE_DATE}",
            null
        )

        val groupedData = mutableMapOf<String, Double>()
        while (cursor.moveToNext()) {
            val date = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_DATE))
            val total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
            groupedData[date] = total
        }
        cursor.close()
        return groupedData
    }
}
