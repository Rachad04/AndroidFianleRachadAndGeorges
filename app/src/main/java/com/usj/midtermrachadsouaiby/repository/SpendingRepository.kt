package com.usj.midtermrachadsouaiby.repository

import com.usj.midtermrachadsouaiby.data.Spending
import com.usj.midtermrachadsouaiby.data.SpendingDatabaseHelper

class SpendingRepository(private val dbHelper: SpendingDatabaseHelper) {

    fun getAllSpendings(): List<Spending> = dbHelper.getAllSpendings()

    fun insertSpending(spending: Spending): Long {
        return dbHelper.insertSpending(spending.description, spending.amount, spending.date)
    }
}
