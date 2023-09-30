package com.jolina.weatherapp.api

import com.jolina.weatherapp.BuildConfig
import com.jolina.weatherapp.model.CityData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * OpenWeatherMap's Geolocation API to allow users to search by city name
 *
 * Given more time it would be good to implement a way to conform user input to
 * this query format: q={city name},{state code},{country code}
 *
 * Entering string "SF, CA, USA" will return a successful response but
 * "SF CA USA" will result in an error
 */

interface GeoLocationApi {
    @GET("geo/1.0/direct?")
    suspend fun getCoords(
        @Query("q") city: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Response<List<CityData>>
}