package com.jolina.weatherapp.repository

import com.jolina.weatherapp.model.Main
import com.jolina.weatherapp.model.NetworkResponseState
import com.jolina.weatherapp.model.OpenWeatherData
import com.jolina.weatherapp.model.Sys
import com.jolina.weatherapp.model.TestNetworkState
import com.jolina.weatherapp.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepositoryImpl(private val state: TestNetworkState, private val weatherListSize: Int = 1) : WeatherRepository {
    override fun getWeather(lat: Double, lon: Double, units: String): Flow<NetworkResponseState> =
        flow {
            when (state) {
                TestNetworkState.LOADING -> {}
                TestNetworkState.SUCCESS -> {
                    emit(
                        NetworkResponseState.Success(
                            OpenWeatherData(
                                weather = if(weatherListSize <= 0) emptyList() else listOf(createTestWeather()),
                                createTestMain(),
                                0L,
                                createTestSys(),
                                ""
                            )
                        )
                    )
                }
                TestNetworkState.ERROR -> emit(NetworkResponseState.Error)
            }
        }

    private fun createTestMain() = Main(
        temp = 0.0,
        feelsLike = 0.0,
        minTemp = 0.0,
        maxTemp = 0.0,
        pressure = 0.0,
        humidity = 0.0,
        seaLevel = 0.0,
        groundLevel = 0.0
    )

    private fun createTestWeather(): Weather = Weather(
        main = "",
        description = "",
        icon = "",
        id = 0
    )

    private fun createTestSys() = Sys(
        sunset = 0L,
        sunrise = 0L
    )
}