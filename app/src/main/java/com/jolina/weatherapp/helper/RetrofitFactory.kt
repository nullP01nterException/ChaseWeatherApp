package com.jolina.weatherapp.helper

import com.jolina.weatherapp.api.GeoLocationApi
import com.jolina.weatherapp.api.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Retrofit factory to make api calls
 */

object RetrofitFactory {
    private const val BASE_URL = "https://api.openweathermap.org/"

    fun makeWeatherService(): WeatherApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(WeatherApi::class.java)

    fun makeGeoLocationService(): GeoLocationApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(GeoLocationApi::class.java)
}