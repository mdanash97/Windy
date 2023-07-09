package com.example.windy.model

import com.example.windy.database.Location
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {
    suspend fun insertData(location: Location)
    suspend fun deleteData(location: Location)
    suspend fun getWeather(latitude:Double,longitude:Double,language:String = "",unit:String = "metric") : Response<WeatherData>
    suspend fun getAllLocations() : Flow<List<Location>>
}