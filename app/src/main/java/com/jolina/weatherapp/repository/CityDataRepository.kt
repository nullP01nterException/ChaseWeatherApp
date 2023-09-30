package com.jolina.weatherapp.repository

import com.jolina.weatherapp.model.NetworkResponseState
import kotlinx.coroutines.flow.Flow

/**
 * For GeoCoder Response
 * Implemented by [CityDataRepositoryImpl] and [FakeCityDataRepositoryImpl]
 */
interface CityDataRepository {
    fun getCityData(city: String): Flow<NetworkResponseState>
}