package com.example.windy.homescreen.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.windy.database.Alerts
import com.example.windy.database.FakeConcreteLocalSource
import com.example.windy.database.Location
import com.example.windy.model.FakeRepo
import com.example.windy.model.Repository
import com.example.windy.network.FakeClientSource
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest{
    val location1 = Location(latitude = 31.205753, longitude = 29.924526, name = "Alexandria")
    val location2 = Location(latitude = 30.033333, longitude = 31.233334, name = "Cairo")
    val location3 = Location(latitude = 21.422510, longitude = 39.826168, name = "Mecca")
    val location4 = Location(latitude = 26.422110, longitude = 33.826168, name = "Random")
    var locations = listOf<Location>(location1,location2,location3)

    val alert1 = Alerts(title = "Alert1", message = "Alert1", time = 12.0.toLong())
    val alert2 = Alerts(title = "Alert2", message = "Alert2", time = 14.0.toLong())
    val alert3 = Alerts(title = "Alert3", message = "Alert3", time = 16.0.toLong())
    val alert4 = Alerts(title = "Alert4", message = "Alert4", time = 18.0.toLong())
    var alerts = listOf<Alerts>(alert1,alert2,alert3)

    lateinit var fakeConcreteLocalSource: FakeConcreteLocalSource
    lateinit var fakeClientSource: FakeClientSource
    lateinit var repository: FakeRepo

    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup(){
        fakeConcreteLocalSource = FakeConcreteLocalSource(locations.toMutableList(),alerts.toMutableList())
        fakeClientSource = FakeClientSource()
        repository = FakeRepo(fakeClientSource,fakeConcreteLocalSource)

        homeViewModel = HomeViewModel(repository)
    }

    @Test
    fun getWeatherData_WeatherData() = runBlockingTest{
        val result = repository.getWeather(31.205753,29.924526)

        assertThat(result.isSuccessful,IsEqual(true))
    }

}