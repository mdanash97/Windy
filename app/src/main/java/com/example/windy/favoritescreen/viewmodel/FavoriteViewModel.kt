package com.example.windy.favoritescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windy.database.DatabaseResult
import com.example.windy.database.Location
import com.example.windy.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private var _locations : MutableStateFlow<DatabaseResult> = MutableStateFlow(DatabaseResult.Loading)
    val locations : StateFlow<DatabaseResult> = _locations

    init {
        viewModelScope.launch (Dispatchers.IO){
            getSavedLocations()
        }
    }

    fun deleteProduct(location: Location){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteData(location)
            getSavedLocations()
        }
    }

    private suspend fun getSavedLocations() {
        repository.getAllLocations().collect{
            _locations.value = DatabaseResult.Success(it)
        }
    }
}