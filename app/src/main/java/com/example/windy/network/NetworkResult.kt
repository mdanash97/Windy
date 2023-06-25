package com.example.windy.network

import com.example.windy.WeatherData

sealed class NetworkResult {
    class Success(val data: WeatherData): NetworkResult()
    class Error(val message: String?): NetworkResult()
    object Loading: NetworkResult()
}