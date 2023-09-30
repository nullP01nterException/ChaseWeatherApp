package com.jolina.weatherapp.repository

import com.jolina.weatherapp.model.NetworkResponseState
import kotlinx.coroutines.flow.Flow

/**
 * For Weather Response
 * Implemented by [WeatherRepositoryImpl] and [FakeWeatherRepositoryImpl]
 */
interface WeatherRepository {
    fun getWeather(lat: Double, lon: Double, units: String): Flow<NetworkResponseState>
}