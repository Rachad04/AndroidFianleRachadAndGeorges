package com.usj.budgettracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.usj.budgettracking.data.Expense
import com.usj.budgettracking.repository.ExpenseRepository
import kotlinx.coroutines.launch

class SpendingViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> get() = _expenses

    /**
     * Fetches all expenses from the repository and updates the LiveData.
     */
    fun fetchAllExpenses() {
        viewModelScope.launch {
            try {
                val allExpenses = repository.getAllExpenses()
                _expenses.postValue(allExpenses)
            } catch (e: Exception) {
                // Handle errors, e.g., logging or showing error messages
            }
        }
    }

    /**
     * Adds a new expense to the repository and refreshes the expense list.
     */
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.insertExpense(expense)
                fetchAllExpenses() // Refresh the list after insertion
            } catch (e: Exception) {
                // Handle errors if necessary
            }
        }
    }

    /**
     * Deletes an expense by ID and refreshes the expense list.
     */
    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expenseId)
                fetchAllExpenses() // Refresh the list after deletion
            } catch (e: Exception) {
                // Handle errors if necessary
            }
        }
    }
}

class SpendingViewModelFactory(private val repository: ExpenseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpendingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpendingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
