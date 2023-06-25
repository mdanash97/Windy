package com.example.windy.network

import com.example.windy.WeatherData
import retrofit2.Response

interface RemoteSource {
    suspend fun getWeather() : Response<WeatherData>
}