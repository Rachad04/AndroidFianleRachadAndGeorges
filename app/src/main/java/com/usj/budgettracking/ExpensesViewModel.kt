package com.usj.budgettracking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usj.budgettracking.data.Expense
import com.usj.budgettracking.repository.BudgetRepository
import com.usj.budgettracking.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpensesViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseRepository: ExpenseRepository = ExpenseRepository(application.applicationContext)
    private val budgetRepository: BudgetRepository = BudgetRepository(application.applicationContext)

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> get() = _expenses

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val expensesList = expenseRepository.getAllExpenses()
                _expenses.postValue(expensesList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addExpense(name: String, amount: Double, categoryName: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val expense = Expense(name = name, amount = amount, category = categoryName, date = date)
                expenseRepository.insertExpense(expense)
                val category = budgetRepository.getCategoryByName(categoryName)
                category?.let {
                    it.currentSpending += amount
                    budgetRepository.updateCategory(it)
                }
                loadExpenses()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch the original expense to compare changes
                val existingExpense = expenseRepository.getExpenseById(expense.id!!)
                if (existingExpense != null) {
                    // Adjust the old category spending
                    val oldCategory = budgetRepository.getCategoryByName(existingExpense.category)
                    oldCategory?.let {
                        it.currentSpending -= existingExpense.amount
                        budgetRepository.updateCategory(it)
                    }

                    // Adjust the new category spending if the category changed
                    if (existingExpense.category != expense.category) {
                        val newCategory = budgetRepository.getCategoryByName(expense.category)
                        newCategory?.let {
                            it.currentSpending += expense.amount
                            budgetRepository.updateCategory(it)
                        }
                    } else {
                        // Update the spending in the same category if only the amount changed
                        val sameCategory = budgetRepository.getCategoryByName(expense.category)
                        sameCategory?.let {
                            it.currentSpending += (expense.amount - existingExpense.amount)
                            budgetRepository.updateCategory(it)
                        }
                    }
                }

                // Update the expense in the database
                expenseRepository.updateExpense(expense)

                // Refresh expenses
                loadExpenses()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val category = budgetRepository.getCategoryByName(expense.category)
                if (category != null) {
                    category.currentSpending -= expense.amount
                    budgetRepository.updateCategory(category)
                }

                expense.id?.let { expenseRepository.deleteExpense(it) }
                loadExpenses()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
