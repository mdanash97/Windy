package com.example.windy.database

import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun insert(location: Location)
    fun getAllLocations(): Flow<List<Location>>
    suspend fun delete(location: Location)

    suspend fun insertAlert(alerts: Alerts)
    fun getAllAlerts(): Flow<List<Alerts>>
    suspend fun deleteAlert(alerts: Alerts)
}