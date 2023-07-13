package com.example.windy.model

import com.example.windy.database.Alerts
import com.example.windy.database.FakeConcreteLocalSource
import com.example.windy.database.Location
import com.example.windy.network.FakeClientSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class RepositoryTest{

    val location1 = Location(latitude = 31.205753, longitude = 29.924526, name = "Alexandria")
    val location2 = Location(latitude = 30.033333, longitude = 31.233334, name = "Cairo")
    val location3 = Location(latitude = 21.422510, longitude = 39.826168, name = "Mecca")
    var locations = listOf<Location>(location1,location2,location3)

    val alert1 = Alerts(title = "Alert1", message = "Alert1", time = 12.0.toLong())
    val alert2 = Alerts(title = "Alert2", message = "Alert2", time = 14.0.toLong())
    val alert3 = Alerts(title = "Alert3", message = "Alert3", time = 16.0.toLong())
    val alert4 = Alerts(title = "Alert4", message = "Alert4", time = 18.0.toLong())
    var alerts = listOf<Alerts>(alert1,alert2,alert3)

    lateinit var fakeConcreteLocalSource: FakeConcreteLocalSource
    lateinit var fakeClientSource: FakeClientSource
    lateinit var repository: Repository

    @Before
    fun setup(){
        //given
        fakeConcreteLocalSource = FakeConcreteLocalSource(locations.toMutableList(),alerts.toMutableList())
        fakeClientSource = FakeClientSource()
        repository = Repository.getInstance(fakeClientSource,fakeConcreteLocalSource)
    }

    @Test
    fun deleteLocation_removeLocation_locationDeleted() = runBlockingTest{
        //when deleting location
         repository.deleteData(location3)

        //then location removed
        assertEquals(2,fakeConcreteLocalSource.locations.size)
    }

    @Test
    fun getAllLocations_locationList() = runBlockingTest{
        //when
        val result = repository.getAllLocations().first()

        //then list of location
        assertThat(result,IsEqual(locations))
    }

    @Test
    fun insertAlert_newAlert_AlertInserted() = runBlockingTest{
        //when inserting new alert
        repository.insertAlert(alert4)

        //then location added
        assertThat(fakeConcreteLocalSource.alert.size,IsEqual(4))
    }

    @Test
    fun deleteAlert_removeAlert_alertRemoved() = runBlockingTest{
        //when removing Alert
        repository.deleteAlert(alert1)

        //then alert removed
        assertThat(fakeConcreteLocalSource.alert.size,IsEqual(2))
    }

    @Test
    fun getAllAlerts_alertList() = runBlockingTest{
        //when
        val result = repository.getAllAlerts().first()

        //then list of alerts
        assertThat(result,IsEqual(alerts))
    }

    @Test
    fun insertLocation_newLocation_locationInserted() = runBlockingTest{

        //given
        val location4 = Location(latitude = 26.422110, longitude = 33.826168, name = "Random")

        //when inserting new location
        repository.insertData(location4)

        //then location added
        assertThat(fakeConcreteLocalSource.locations.size,IsEqual(4))
    }

    @Test
    fun getWeatherData_weatherData() = runBlockingTest{
        //when
        val weather = repository.getWeather(31.205753,29.924526)

        //then weatherData returned
        assertThat(weather.isSuccessful,IsEqual(true))
    }

}