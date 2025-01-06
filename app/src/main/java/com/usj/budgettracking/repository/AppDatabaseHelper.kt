package com.usj.budgettracking.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.usj.budgettracking.data.BudgetCategory
import com.usj.budgettracking.data.Expense

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "app_database.db"
        const val DATABASE_VERSION = 1

        const val TABLE_BUDGET_CATEGORY = "budget_category"
        const val COLUMN_BUDGET_ID = "_id"
        const val COLUMN_BUDGET_NAME = "name"
        const val COLUMN_BUDGET_AMOUNT = "budget"
        const val COLUMN_BUDGET_SPENT = "current_spending"

        const val TABLE_EXPENSES = "expenses"
        const val COLUMN_EXPENSE_ID = "id"
        const val COLUMN_EXPENSE_NAME = "name"
        const val COLUMN_EXPENSE_AMOUNT = "amount"
        const val COLUMN_EXPENSE_CATEGORY = "category"
        const val COLUMN_EXPENSE_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createBudgetCategoryTable = """
            CREATE TABLE $TABLE_BUDGET_CATEGORY (
                $COLUMN_BUDGET_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BUDGET_NAME TEXT NOT NULL UNIQUE,
                $COLUMN_BUDGET_AMOUNT REAL NOT NULL,
                $COLUMN_BUDGET_SPENT REAL NOT NULL DEFAULT 0
            )
        """
        val createExpensesTable = """
            CREATE TABLE $TABLE_EXPENSES (
                $COLUMN_EXPENSE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EXPENSE_NAME TEXT NOT NULL,
                $COLUMN_EXPENSE_AMOUNT REAL NOT NULL,
                $COLUMN_EXPENSE_CATEGORY TEXT NOT NULL,
                $COLUMN_EXPENSE_DATE TEXT NOT NULL
            )
        """
        db?.execSQL(createBudgetCategoryTable)
        db?.execSQL(createExpensesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BUDGET_CATEGORY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        onCreate(db)
    }
}
