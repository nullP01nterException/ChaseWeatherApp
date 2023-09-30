package com.jolina.weatherapp.api

import com.jolina.weatherapp.BuildConfig
import com.jolina.weatherapp.model.OpenWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * OpenWeatherMap's Weather API that returns temperature in units that are commonly used
 * to the country set in the user's device locale
 */

interface WeatherApi {
    @GET("data/2.5/weather?")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Response<OpenWeatherData>
}