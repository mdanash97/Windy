package com.example.windy.network

import com.example.windy.model.WeatherData
import retrofit2.Response

interface RemoteSource {
    suspend fun getWeather(latitude:Double,longitude:Double,language:String = "",unit:String = "",key:String = "43ec8dc1817e1063d26f5559da136d4d") : Response<WeatherData>
}