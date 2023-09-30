package com.jolina.weatherapp.model.local

/**
 * Local model of consolidated temperature data for UI layer to use
 */

data class Temperature(
    val units: String,
    val temp: Double,
    val feelsLike: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val pressure: Double,
    val humidity: Double,
    val seaLevel: Double,
    val groundLevel: Double,
)
