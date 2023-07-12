package com.example.windy.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windy.alerts.view.AlertScheduler
import com.example.windy.database.Alerts
import com.example.windy.database.DatabaseResult
import com.example.windy.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private var _alerts: MutableStateFlow<DatabaseResult> = MutableStateFlow(DatabaseResult.Loading)
    val alerts : StateFlow<DatabaseResult> = _alerts

    init {
        viewModelScope.launch (Dispatchers.IO){
            getSavedAlerts()
        }
    }

    fun deleteAlert(alerts: Alerts){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAlert(alerts)
            getSavedAlerts()
        }
    }

    private suspend fun getSavedAlerts() {
        repository.getAllAlerts().collect{
            _alerts.value = DatabaseResult.SuccessAlerts(it)
        }
    }

    fun insertAlert(alerts: Alerts) {
        viewModelScope.launch {
            repository.insertAlert(alerts)
        }
    }
}