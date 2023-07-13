package com.example.windy.model

import com.example.windy.database.Alerts
import com.example.windy.database.FakeConcreteLocalSource
import com.example.windy.database.Location
import com.example.windy.network.FakeClientSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FakeRepo(var fakeClientSource: FakeClientSource,var fakeConcreteLocalSource: FakeConcreteLocalSource) : RepositoryInterface {
    override suspend fun insertData(location: Location) {
        fakeConcreteLocalSource.insert(location)
    }

    override suspend fun deleteData(location: Location) {
        fakeConcreteLocalSource.delete(location)
    }

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        unit: String
    ): Response<WeatherData> {
        return fakeClientSource.getWeather(latitude,longitude)
    }

    override suspend fun getAllLocations(): Flow<List<Location>> {
        return fakeConcreteLocalSource.getAllLocations()
    }

    override suspend fun insertAlert(alerts: Alerts) {
        fakeConcreteLocalSource.insertAlert(alerts)
    }

    override suspend fun deleteAlert(alerts: Alerts) {
        fakeConcreteLocalSource.deleteAlert(alerts)
    }

    override suspend fun getAllAlerts(): Flow<List<Alerts>> {
        return fakeConcreteLocalSource.getAllAlerts()
    }
}