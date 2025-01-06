package com.usj.budgettracking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usj.budgettracking.data.BudgetCategory
import com.usj.budgettracking.repository.BudgetRepository
import com.usj.budgettracking.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val budgetRepository = BudgetRepository(application)
    private val expenseRepository = ExpenseRepository(application)

    private val _categories = MutableLiveData<List<BudgetCategory>>()
    val categories: LiveData<List<BudgetCategory>> get() = _categories

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        refreshCategories()
    }

    fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = budgetRepository.getAllCategories()
                _categories.postValue(categories)
            } catch (e: Exception) {
                setError("Failed to fetch categories: ${e.message}")
            }
        }
    }

    fun insertCategory(category: BudgetCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = budgetRepository.insertCategory(category)
                if (result != -1L) {
                    refreshCategories() // Reload categories after insertion
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateCategory(category: BudgetCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch the existing category
                val existingCategory = budgetRepository.getCategoryByName(category.name)

                if (existingCategory != null && existingCategory.name != category.name) {
                    // Update the category name in all associated expenses
                    expenseRepository.updateExpensesCategoryName(existingCategory.name, category.name)
                }

                // Update the category details
                val result = budgetRepository.updateCategory(category)
                if (result > 0) {
                    refreshCategories() // Reload categories after update
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun deleteCategory(category: BudgetCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Delete all expenses linked to this category
                expenseRepository.deleteExpensesByCategory(category.name)

                // Delete the category itself
                val result = budgetRepository.deleteCategory(category)
                if (result > 0) {
                    refreshCategories() // Reload the category list after deletion
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun setError(message: String?) {
        _error.postValue(message)
    }
}
