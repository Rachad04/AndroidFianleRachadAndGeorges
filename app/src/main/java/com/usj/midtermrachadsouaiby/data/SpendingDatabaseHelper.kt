package com.usj.midtermrachadsouaiby.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SpendingDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "spending.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "Spending"
        const val COLUMN_ID = "id"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL,
                $COLUMN_DATE TEXT NOT NULL
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertSpending(description: String, amount: Double, date: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_DATE, date)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllSpendings(): List<Spending> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE DESC")
        val spendings = mutableListOf<Spending>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            spendings.add(Spending(id, description, amount, date))
        }
        cursor.close()
        return spendings
    }
}
