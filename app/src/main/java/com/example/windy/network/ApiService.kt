package com.example.windy.network

import com.example.windy.model.WeatherData
import org.intellij.lang.annotations.Language
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{
    @GET("onecall")
    suspend fun getWeather(@Query("lat") latitude:Double,
                           @Query("lon") longitude:Double,
                           @Query("lang") language:String,
                           @Query("units") unit:String,
                           @Query("appid") key:String): Response<WeatherData>
}

object RetrofitHelper{
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}




