package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repository) : ViewModel() {

    private val _monthlyCount = MutableStateFlow(0)
    val monthlyCount: StateFlow<Int> = _monthlyCount

    fun refreshMonthly() {
        viewModelScope.launch(Dispatchers.IO) {
            val n = repo.countMonth()
            _monthlyCount.value = n
        }
    }

    fun addEventNow() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNow()
            val n = repo.countMonth()
            _monthlyCount.value = n
        }
    }
}