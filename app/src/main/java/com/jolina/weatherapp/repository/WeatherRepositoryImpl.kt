package com.jolina.weatherapp.repository

import com.jolina.weatherapp.helper.RetrofitFactory
import com.jolina.weatherapp.model.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

/**
 * Emits NetworkState based on how the API response looks
 */
class WeatherRepositoryImpl : WeatherRepository {
    private val service = RetrofitFactory.makeWeatherService()

    override fun getWeather(lat: Double, lon: Double, units: String): Flow<NetworkResponseState> = flow {
        val response = service.getWeather(lat, lon, units)
        try {
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResponseState.Success(response.body()!!))
            }
            else emit(NetworkResponseState.Error)
        } catch (e: IOException) {
            emit(NetworkResponseState.Error)
        }
    }
}