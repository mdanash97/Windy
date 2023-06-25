package com.example.windy.network

import com.example.windy.WeatherData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService{
    @GET("")
    suspend fun getWeather(): Response<WeatherData>
}

object RetrofitHelper{
    private const val BASE_URL = ""
    val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}




