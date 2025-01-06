package com.usj.midtermrachadsouaiby.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.usj.midtermrachadsouaiby.data.Spending
import com.usj.midtermrachadsouaiby.repository.SpendingRepository
import kotlinx.coroutines.launch

class SpendingViewModel(private val repository: SpendingRepository) : ViewModel() {
    private val _spendings = MutableLiveData<List<Spending>>()
    val spendings: LiveData<List<Spending>> get() = _spendings

    fun fetchAllSpendings() {
        viewModelScope.launch {
            try {
                _spendings.postValue(repository.getAllSpendings())
            } catch (e: Exception) {
                // Handle errors if necessary (e.g., logging or notifying the UI)
            }
        }
    }

    fun addSpending(spending: Spending) {
        viewModelScope.launch {
            try {
                repository.insertSpending(spending)
                fetchAllSpendings()
            } catch (e: Exception) {
                // Handle errors if necessary
            }
        }
    }
}

class SpendingViewModelFactory(private val repository: SpendingRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpendingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpendingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
