package com.example.windy.network

import com.example.windy.model.*
import retrofit2.Response

class FakeClientSource : RemoteSource {
    val currentWeather = CurrentWeatherData(
        dt = 1626180000L,
        sunrise = 1626132000L,
        sunset = 1626189600L,
        temp = 27.5,
        feels_like = 29.3,
        pressure = 1012,
        humidity = 65,
        dew_point = 20.6,
        uvi = 8.5,
        clouds = 75,
        visibility = 10000,
        wind_speed = 4.12,
        wind_deg = 120,
        wind_gust = 5.51,
        weather = listOf(
            Weather(
                id = 803,
                main = "Clouds",
                description = "broken clouds",
                icon = "04d"
            )
        )
    )

    val minutelyWeather = listOf(
        MinutelyWeatherData(
            dt = 1626180060L,
            precipitation = 0
        ),
        MinutelyWeatherData(
            dt = 1626180120L,
            precipitation = 0
        ),
        MinutelyWeatherData(
            dt = 1626180180L,
            precipitation = 0
        ),
        // ...
    )

    val hourlyWeather = listOf(
        HourlyWeatherData(
            dt = 1626177600L,
            temp = 26.77,
            feels_like = 28.66,
            pressure = 1012,
            humidity = 73,
            dew_point = 20.83,
            uvi = 8.5,
            clouds = 74,
            visibility = 10000,
            wind_speed = 4.12,
            wind_deg = 120,
            wind_gust = 5.51,
            weather = listOf(
                Weather(
                    id = 803,
                    main = "Clouds",
                    description = "broken clouds",
                    icon = "04d"
                )
            ),
            pop = 0.1
        ),
        HourlyWeatherData(
            dt = 1626181200L,
            temp = 27.5,
            feels_like = 29.3,
            pressure = 1012,
            humidity = 65,
            dew_point = 20.6,
            uvi = 8.5,
            clouds = 75,
            visibility = 10000,
            wind_speed = 4.12,
            wind_deg = 120,
            wind_gust = 5.51,
            weather = listOf(
                Weather(
                    id = 803,
                    main = "Clouds",
                    description = "broken clouds",
                    icon = "04d"
                )
            ),
            pop = 0.15
        ),
        // ...
    )

    val dailyWeather = listOf(
        DailyWeatherData(
            dt = 1626158400L,
            sunrise = 1626132000L,
            sunset = 1626189600L,
            moonrise = 1626130980L,
            moonset = 1626189540L,
            moon_phase = 0.57,
            summary = "Partly cloudy throughout the day.",
            temp = TempData(
                day = 27.5,
                min = 25.4,
                max = 28.5,
                night = 25.4,
                eve = 26.8,
                morn = 25.9
            ),
            feels_like = FeelsLikeData(
                day = 29.3,
                night = 26.3,
                eve = 28.3,
                morn = 27.5
            ),
            pressure = 1012,
            humidity = 65,
            dew_point = 20.6,
            wind_speed = 4.12,
            wind_deg = 120,
            wind_gust = 5.51,
            weather = listOf(
                Weather(
                    id = 803,
                    main = "Clouds",
                    description = "broken clouds",
                    icon = "04d"
                )
            ),
            clouds = 75,
            pop = 0.3,
            rain = null,
            uvi = 8.5
        ),
        DailyWeatherData(
            dt = 1626244800L,
            sunrise = 1626218400L,
            sunset = 1626276000L,
            moonrise = 1626220800L,
            moonset = 1626279600L,
            moon_phase = 0.6,
            summary = "Mostly cloudy throughout the day.",
            temp = TempData(
                day = 28.2,
                min = 25.5,
                max = 29.3,
                night = 25.5,
                eve = 27.7,
                morn = 26.1
            ),
            feels_like = FeelsLikeData(
                day = 30.1,
                night = 26.6,
                eve = 29.0,
                morn = 27.3
            ),
            pressure = 1012,
            humidity = 70,
            dew_point = 21.5,
            wind_speed = 4.55,
            wind_deg = 130,
            wind_gust = 6.02,
            weather = listOf(
                Weather(
                    id = 803,
                    main = "Clouds",
                    description = "broken clouds",
                    icon = "04d"
                )
            ),
            clouds = 80,
            pop = 0.4,
            rain = null,
            uvi = 8.1
        ),
        // ...
    )

    val alerts = listOf(
        Alert(
            sender_name = "National Weather Service",
            event = "Heat Advisory",
            start = 1626184800L,
            end = 1626206400L,
            description = "...HEAT ADVISORY REMAINS IN EFFECT FROM NOON TODAY TO 8 PM CDT THIS EVENING...",
            tags = listOf("Extreme temperature")
        ),
        Alert(
            sender_name = "National Weather Service",
            event = "Flood Warning",
            start = 1626184800L,
            end = 1626206400L,
            description = "The Flood Warning continues for...",
            tags = listOf("Flooding")
        )
    )

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        unit: String,
        key: String
    ): Response<WeatherData> {
        val weatherData = WeatherData(
            lat = 37.7749,
            lon = -122.4194,
            timezone = "America/Los_Angeles",
            timezone_offset = -25200,
            current = currentWeather,
            minutely = minutelyWeather,
            hourly = hourlyWeather,
            daily = dailyWeather,
            alerts = alerts
        )
        return Response.success(weatherData)
    }
}