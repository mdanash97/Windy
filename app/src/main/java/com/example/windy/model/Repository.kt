package com.example.windy.model

import com.example.windy.database.LocalSource
import com.example.windy.database.Location
import com.example.windy.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repository private constructor(private val remoteSource: RemoteSource, private val localSource: LocalSource) : RepositoryInterface {

    companion object{
        private var instance:Repository? = null
        fun getInstance(remoteSource: RemoteSource,localSource: LocalSource):Repository{
            return instance ?: synchronized(this){
                val temp = Repository(remoteSource,localSource)
                temp
            }
        }
    }

    override suspend fun insertData(location: Location) {
        localSource.insert(location)
    }

    override suspend fun deleteData(location: Location) {
        localSource.delete(location)
    }

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        unit: String
    ): Response<WeatherData> {
        return remoteSource.getWeather(latitude,longitude,language,unit)
    }

    override suspend fun getAllLocations(): Flow<List<Location>> {
        return localSource.getAllLocations()
    }
}