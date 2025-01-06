package com.usj.budgettracking.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.usj.budgettracking.data.Expense
import com.usj.budgettracking.database.AppDatabaseHelper

class ExpenseRepository(context: Context) {

    private val dbHelper = AppDatabaseHelper(context)

    fun getAllExpenses(): List<Expense> {
        val db = dbHelper.readableDatabase
        val expensesList = mutableListOf<Expense>()

        val query = """
            SELECT 
                ${AppDatabaseHelper.COLUMN_EXPENSE_ID} AS id,
                ${AppDatabaseHelper.COLUMN_EXPENSE_NAME} AS name,
                ${AppDatabaseHelper.COLUMN_EXPENSE_AMOUNT} AS amount,
                ${AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY} AS category,
                ${AppDatabaseHelper.COLUMN_EXPENSE_DATE} AS date
            FROM ${AppDatabaseHelper.TABLE_EXPENSES}
        """
        val cursor: Cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                expensesList.add(
                    Expense(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        amount = it.getDouble(it.getColumnIndexOrThrow("amount")),
                        category = it.getString(it.getColumnIndexOrThrow("category")),
                        date = it.getString(it.getColumnIndexOrThrow("date"))
                    )
                )
            }
        }
        return expensesList
    }

    fun insertExpense(expense: Expense): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_EXPENSE_NAME, expense.name)
            put(AppDatabaseHelper.COLUMN_EXPENSE_AMOUNT, expense.amount)
            put(AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY, expense.category)
            put(AppDatabaseHelper.COLUMN_EXPENSE_DATE, expense.date)
        }
        return db.insert(AppDatabaseHelper.TABLE_EXPENSES, null, values)
    }

    fun deleteExpense(expenseId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AppDatabaseHelper.TABLE_EXPENSES,
            "${AppDatabaseHelper.COLUMN_EXPENSE_ID} = ?",
            arrayOf(expenseId.toString())
        )
    }

    fun deleteExpensesByCategory(categoryName: String) {
        val db = dbHelper.writableDatabase
        db.delete(
            AppDatabaseHelper.TABLE_EXPENSES,
            "${AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY} = ?",
            arrayOf(categoryName)
        )
    }

    fun updateExpensesCategoryName(oldCategoryName: String, newCategoryName: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY, newCategoryName)
        }
        db.update(
            AppDatabaseHelper.TABLE_EXPENSES,
            values,
            "${AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY} = ?",
            arrayOf(oldCategoryName)
        )
    }


    fun updateExpense(expense: Expense): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_EXPENSE_NAME, expense.name)
            put(AppDatabaseHelper.COLUMN_EXPENSE_AMOUNT, expense.amount)
            put(AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY, expense.category)
            put(AppDatabaseHelper.COLUMN_EXPENSE_DATE, expense.date)
        }
        return db.update(
            AppDatabaseHelper.TABLE_EXPENSES,
            values,
            "${AppDatabaseHelper.COLUMN_EXPENSE_ID} = ?",
            arrayOf(expense.id.toString())
        )
    }

    fun getExpenseById(expenseId: Int): Expense? {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT * FROM ${AppDatabaseHelper.TABLE_EXPENSES} 
            WHERE ${AppDatabaseHelper.COLUMN_EXPENSE_ID} = ?
        """
        val cursor = db.rawQuery(query, arrayOf(expenseId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                return Expense(
                    id = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_NAME)),
                    amount = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_AMOUNT)),
                    category = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_CATEGORY)),
                    date = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EXPENSE_DATE))
                )
            }
        }
        return null
    }
}
