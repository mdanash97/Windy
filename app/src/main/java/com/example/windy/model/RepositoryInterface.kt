package com.example.windy.model

import com.example.windy.database.Location
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {
    suspend fun insertData(location: Location)
    suspend fun deleteData(location: Location)
    suspend fun getWeather(longitude:Double,latitude:Double,language:String = "",unit:String = "") : Response<WeatherData>
    suspend fun getAllLocations() : Flow<List<Location>>
}