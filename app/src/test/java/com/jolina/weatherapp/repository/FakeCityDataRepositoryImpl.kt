package com.jolina.weatherapp.repository

import com.jolina.weatherapp.model.CityData
import com.jolina.weatherapp.model.NetworkResponseState
import com.jolina.weatherapp.model.TestNetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCityDataRepositoryImpl(private val state: TestNetworkState, private val locationListSize: Int = 1): CityDataRepository {
    override fun getCityData(city: String): Flow<NetworkResponseState> = flow {
        when(state){
            TestNetworkState.LOADING -> {}
            TestNetworkState.SUCCESS -> {
                emit(NetworkResponseState.Success(if(locationListSize <= 0) emptyList() else listOf(createTestCityData())))
            }
            TestNetworkState.ERROR -> emit(NetworkResponseState.Error)
        }
    }

    private fun createTestCityData() = CityData(
        name = "test",
        lat = 0.0,
        lon = 0.0,
    )
}