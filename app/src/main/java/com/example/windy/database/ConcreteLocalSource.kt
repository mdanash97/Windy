package com.example.windy.database

import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(private val locationDAO: LocationDAO) : LocalSource {
    override suspend fun insert(location: Location) {
        locationDAO.insertLocation(location)
    }

    override fun getAllLocations(): Flow<List<Location>> {
        return locationDAO.getAllLocations()
    }

    override suspend fun delete(location: Location) {
        locationDAO.deleteLocation(location)
    }
}