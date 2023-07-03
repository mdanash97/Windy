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
        longitude: Double,
        latitude: Double,
        language: String,
        unit: String,
        key: String
    ): Response<WeatherData> {
        return weatherService.getWeather(longitude,latitude,language,unit,key)
    }
}

