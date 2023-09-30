package com.jolina.weatherapp.model.local

import com.jolina.weatherapp.model.OpenWeatherData
import com.jolina.weatherapp.model.Sys

/**
 * Local model for UI layer to use that combines data from [OpenWeatherData] and [Sys]
 */

data class System(val date: String, val sunrise: String, val sunset: String)
