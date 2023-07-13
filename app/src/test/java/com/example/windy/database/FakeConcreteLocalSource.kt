package com.example.windy.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeConcreteLocalSource(var locations : MutableList<Location> = mutableListOf(),var alert: MutableList<Alerts> = mutableListOf()) : LocalSource {
    override suspend fun insert(location: Location) {
        locations.add(location)
    }

    override fun getAllLocations(): Flow<List<Location>> {
        return flow {
            emit(locations)
        }
    }

    override suspend fun delete(location: Location) {
        locations.remove(location)
    }

    override suspend fun insertAlert(alerts: Alerts) {
        alert.add(alerts)
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return flow {
            emit(alert)
        }
    }

    override suspend fun deleteAlert(alerts: Alerts) {
        alert.remove(alerts)
    }
}