package com.example.windy.mapactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windy.database.Location
import com.example.windy.model.RepositoryInterface
import kotlinx.coroutines.launch

class MapViewModel(private val repository: RepositoryInterface) : ViewModel() {
    fun insertFav(location: Location) {
        viewModelScope.launch {
            repository.insertData(location)
        }
    }
}