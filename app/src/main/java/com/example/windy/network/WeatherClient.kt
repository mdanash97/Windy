package com.example.windy.network

import com.example.windy.model.WeatherData
import retrofit2.Response

class WeatherClient private constructor(): RemoteSource {

    private val weatherService : ApiService by lazy{
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    companion object{
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient {
            return instance ?: synchronized(this){
                val temp = WeatherClient()
                temp
            }
        }
    }

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        unit: String,
        key: String
    ): Response<WeatherData> {
        return weatherService.getWeather(latitude,longitude,language,unit,key)
    }
}

