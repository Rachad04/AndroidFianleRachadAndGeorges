package com.usj.budgettracking.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BudgetDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "budget_database.db"
        const val DATABASE_VERSION = 2

        const val TABLE_NAME = "budget_category"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_BUDGET = "budget"
        const val COLUMN_CURRENT_SPENDING = "currentSpending"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_BUDGET REAL NOT NULL,
                $COLUMN_CURRENT_SPENDING REAL DEFAULT 0.0
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
        Log.d("BudgetDatabaseHelper", "Table $TABLE_NAME created successfully.")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("BudgetDatabaseHelper", "Upgrading database from version $oldVersion to $newVersion")
        if (oldVersion < 2) {
            val alterTableQuery = """
                ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_CURRENT_SPENDING REAL DEFAULT 0.0
            """
            db?.execSQL(alterTableQuery)
            Log.d("BudgetDatabaseHelper", "Column $COLUMN_CURRENT_SPENDING added successfully.")
        }
    }
}
