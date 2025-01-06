package com.usj.budgettracking.repository

import android.content.ContentValues
import android.content.Context
import com.usj.budgettracking.data.BudgetCategory
import com.usj.budgettracking.database.AppDatabaseHelper

class BudgetRepository(context: Context) {

    private val dbHelper = AppDatabaseHelper(context.applicationContext)

    // Fetch all categories
    fun getAllCategories(): List<BudgetCategory> {
        val db = dbHelper.readableDatabase
        val categories = mutableListOf<BudgetCategory>()
        val cursor = db.rawQuery("SELECT * FROM ${AppDatabaseHelper.TABLE_BUDGET_CATEGORY}", null)
        cursor.use {
            while (it.moveToNext()) {
                categories.add(
                    BudgetCategory(
                        id = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_ID)),
                        name = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_NAME)),
                        budget = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_AMOUNT)),
                        currentSpending = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_SPENT))
                    )
                )
            }
        }
        return categories
    }

    // Insert a new category
    fun insertCategory(category: BudgetCategory): Long {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put(AppDatabaseHelper.COLUMN_BUDGET_NAME, category.name)
                put(AppDatabaseHelper.COLUMN_BUDGET_AMOUNT, category.budget)
                put(AppDatabaseHelper.COLUMN_BUDGET_SPENT, category.currentSpending)
            }
            val result = db.insert(AppDatabaseHelper.TABLE_BUDGET_CATEGORY, null, values)
            if (result == -1L) {
                throw Exception("Failed to insert category: ${category.name}")
            }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            -1L // Return -1 to indicate failure
        }
    }

    // Update an existing category
    fun updateCategory(category: BudgetCategory): Int {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put(AppDatabaseHelper.COLUMN_BUDGET_NAME, category.name)
                put(AppDatabaseHelper.COLUMN_BUDGET_AMOUNT, category.budget)
                put(AppDatabaseHelper.COLUMN_BUDGET_SPENT, category.currentSpending)
            }
            db.update(
                AppDatabaseHelper.TABLE_BUDGET_CATEGORY,
                values,
                "${AppDatabaseHelper.COLUMN_BUDGET_ID} = ?",
                arrayOf(category.id.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
            0 // Return 0 to indicate failure
        }
    }

    // Fetch a category by name
    fun getCategoryByName(name: String): BudgetCategory? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${AppDatabaseHelper.TABLE_BUDGET_CATEGORY} WHERE ${AppDatabaseHelper.COLUMN_BUDGET_NAME} = ?",
            arrayOf(name)
        )
        cursor.use {
            return if (it.moveToFirst()) {
                BudgetCategory(
                    id = it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_NAME)),
                    budget = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_AMOUNT)),
                    currentSpending = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_BUDGET_SPENT))
                )
            } else null
        }
    }

    // Delete a category
    fun deleteCategory(category: BudgetCategory): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AppDatabaseHelper.TABLE_BUDGET_CATEGORY,
            "${AppDatabaseHelper.COLUMN_BUDGET_NAME} = ?",
            arrayOf(category.name)
        )
    }

}
