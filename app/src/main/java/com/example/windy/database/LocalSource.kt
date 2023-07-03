package com.example.windy.database

import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun insert(location: Location)
    fun getAllLocations(): Flow<List<Location>>
    suspend fun delete(location: Location)
}