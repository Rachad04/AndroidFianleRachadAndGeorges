package com.usj.budgettracking.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "expenses.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "expenses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_EXPENSES_TABLE = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_AMOUNT REAL," +
                "$COLUMN_CATEGORY TEXT," +
                "$COLUMN_DATE TEXT)")
        db?.execSQL(CREATE_EXPENSES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Add a new expense to the database
    fun addExpense(expense: Expense): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, expense.name)
            put(COLUMN_AMOUNT, expense.amount)
            put(COLUMN_CATEGORY, expense.category)
            put(COLUMN_DATE, expense.date)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Update an existing expense
    fun updateExpense(expense: Expense): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, expense.name)
            put(COLUMN_AMOUNT, expense.amount)
            put(COLUMN_CATEGORY, expense.category)
            put(COLUMN_DATE, expense.date)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(expense.id.toString()))
    }

    // Delete an expense
    fun deleteExpense(id: Int?): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Get all expenses
    fun getAllExpenses(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE DESC")
    }

    // Get expenses by category
    fun getExpensesByCategory(category: String): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_NAME, null, "$COLUMN_CATEGORY = ?", arrayOf(category), null, null, "$COLUMN_DATE DESC")
    }
}
