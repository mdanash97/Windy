package com.example.windy.database

import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(private val locationDAO: LocationDAO,private val alertsDAO: AlertsDAO) : LocalSource {
    override suspend fun insert(location: Location) {
        locationDAO.insertLocation(location)
    }

    override fun getAllLocations(): Flow<List<Location>> {
        return locationDAO.getAllLocations()
    }

    override suspend fun delete(location: Location) {
        locationDAO.deleteLocation(location)
    }

    override suspend fun insertAlert(alerts: Alerts) {
        alertsDAO.insertAlert(alerts)
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return alertsDAO.getAllAlerts()
    }

    override suspend fun deleteAlert(alerts: Alerts) {
        alertsDAO.deleteAlert(alerts)
    }
}