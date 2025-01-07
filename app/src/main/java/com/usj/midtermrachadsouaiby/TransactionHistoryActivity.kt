package com.usj.midtermrachadsouaiby

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TransactionHistoryActivity : AppCompatActivity() {

    private val transactions = mutableListOf<Triple<String, String, String>>() // List to store transactions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_history)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        val transactionListLayout = findViewById<LinearLayout>(R.id.transactionListLayout)

        // Swipe refresh logic
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            // You can refresh transaction data here
        }

        // Floating Action Button click logic
        val addTransactionButton = findViewById<FloatingActionButton>(R.id.fabAddTransaction)
        addTransactionButton.setOnClickListener {
            showAddTransactionDialog(transactionListLayout)
        }

        // Dummy transaction data (initial list)
        transactions.addAll(
            listOf(
                Triple("Groceries", "$50", "2025-01-06"),
                Triple("Transport", "$15", "2025-01-05"),
                Triple("Utilities", "$80", "2025-01-04")
            )
        )

        // Add transaction data dynamically to the LinearLayout
        refreshTransactionList(transactionListLayout)
    }

    private fun showAddTransactionDialog(transactionListLayout: LinearLayout) {
        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null)

        // Initialize the input fields
        val activityInput = dialogView.findViewById<EditText>(R.id.activityInput)
        val priceInput = dialogView.findViewById<EditText>(R.id.priceInput)
        val dateInput = dialogView.findViewById<EditText>(R.id.dateInput)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Transaction")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val activity = activityInput.text.toString()
                var price = priceInput.text.toString()
                val date = dateInput.text.toString()

                if (activity.isNotEmpty() && price.isNotEmpty() && date.isNotEmpty()) {
                    // Ensure the price starts with a "$"
                    if (!price.startsWith("$")) {
                        price = "$$price"
                    }

                    // Format the date to YYYY-MM-DD
                    val formattedDate = formatDate(date)

                    if (formattedDate != null) {
                        // Add the new transaction to the list
                        transactions.add(Triple(activity, price, formattedDate))

                        // Refresh the transaction list
                        refreshTransactionList(transactionListLayout)

                        Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Invalid date format! Use YYYYMMDD or YYYY/MM/DD.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun refreshTransactionList(transactionListLayout: LinearLayout) {
        // Clear the existing views
        transactionListLayout.removeAllViews()

        // Add each transaction to the layout
        for (transaction in transactions) {
            val transactionView = layoutInflater.inflate(R.layout.transaction_item, null)

            val description = transactionView.findViewById<TextView>(R.id.transactionDescription)
            val amount = transactionView.findViewById<TextView>(R.id.transactionAmount)
            val date = transactionView.findViewById<TextView>(R.id.transactionDate)

            description.text = transaction.first
            amount.text = transaction.second
            date.text = transaction.third

            transactionListLayout.addView(transactionView)
        }
    }

    // Helper function to format the date
    private fun formatDate(date: String): String? {
        // Check if the input is in YYYYMMDD or YYYY/MM/DD format
        val regex = Regex("^\\d{4}[-/]?\\d{2}[-/]?\\d{2}$")
        return if (regex.matches(date)) {
            // Remove any slashes or dashes and reformat as YYYY-MM-DD
            date.replace(Regex("[-/]"), "").let {
                "${it.substring(0, 4)}-${it.substring(4, 6)}-${it.substring(6, 8)}"
            }
        } else {
            null // Return null if the input is invalid
        }
    }
}
