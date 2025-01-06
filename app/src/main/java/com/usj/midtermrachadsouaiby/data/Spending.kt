package com.usj.midtermrachadsouaiby.data

data class Spending(
    val id: Int = 0,                // Primary key (optional, handled by SQLite)
    val description: String,        // Description of the spending
    val amount: Double,             // Amount spent
    val date: String                // Date of the transaction
)
