package com.usj.budgettracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Expense(
    val id: Int? = null,
    val name: String,
    val amount: Double,
    val category: String,
    var date: String
)
