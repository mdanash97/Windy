package com.example.windy.homescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windy.database.Location
import com.example.windy.model.RepositoryInterface
import com.example.windy.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private var _weatherData: MutableStateFlow<NetworkResult> = MutableStateFlow(NetworkResult.Loading)
    val weatherData: StateFlow<NetworkResult> = _weatherData
    private var longitude:Double = 0.0
    private var latitude:Double = 0.0

    init {
        viewModelScope.launch {
            val response = repository.getWeather(latitude,longitude)
            if (response.isSuccessful) {
                response.body()?.let {
                    _weatherData.value = NetworkResult.Success(it)
                }
            }else{
                _weatherData.value = NetworkResult.Error("Something Went Wrong")
            }
        }
    }

    fun insertFav(location: Location) {
        viewModelScope.launch {
            repository.insertData(location)
        }
    }
    fun setLocation(longitude:Double,latitude:Double){
        this.longitude = longitude
        this.latitude = latitude
    }
}