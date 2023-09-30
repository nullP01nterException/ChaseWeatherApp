package com.jolina.weatherapp.repository

import com.jolina.weatherapp.helper.RetrofitFactory
import com.jolina.weatherapp.model.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

/**
 * Emits NetworkState based on how the API response looks
 */
class CityDataRepositoryImpl : CityDataRepository {
    private val service = RetrofitFactory.makeGeoLocationService()

    override fun getCityData(city: String): Flow<NetworkResponseState> = flow {
        val result = service.getCoords(city)
        try {
            if (result.isSuccessful && result.body() != null) {
                emit(NetworkResponseState.Success(result.body()!!))
            } else emit(NetworkResponseState.Error)
        } catch (e: IOException) {
            emit(NetworkResponseState.Error)
        }
    }
}