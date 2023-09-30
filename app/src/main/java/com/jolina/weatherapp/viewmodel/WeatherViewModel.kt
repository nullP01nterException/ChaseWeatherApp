package com.jolina.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jolina.weatherapp.helper.DateTimeHelper
import com.jolina.weatherapp.model.CityData
import com.jolina.weatherapp.model.NetworkResponseState
import com.jolina.weatherapp.model.OpenWeatherData
import com.jolina.weatherapp.model.Weather
import com.jolina.weatherapp.model.local.System
import com.jolina.weatherapp.model.local.Temperature
import com.jolina.weatherapp.repository.CityDataRepository
import com.jolina.weatherapp.repository.CityDataRepositoryImpl
import com.jolina.weatherapp.repository.WeatherRepository
import com.jolina.weatherapp.repository.WeatherRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.Locale

data class WeatherViewState(
    val loading: Boolean = false,
    val cityName: String = "",
    val weather: Weather? = null,
    val temperature: Temperature? = null,
    val system: System? = null,
    val hasError: Boolean = false,
    val wasSearched: Boolean = false,
)

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val cityDataRepository: CityDataRepository
) : ViewModel() {

    private val _weatherState: MutableStateFlow<WeatherViewState> by lazy {
        MutableStateFlow(WeatherViewState())
    }
    val weatherState: StateFlow<WeatherViewState> get() = _weatherState

    fun getLocationData(city: String, locale: Locale) {
        _weatherState.update { it.copy(loading = true, wasSearched = true, hasError = false) }
        cityDataRepository.getCityData(city).onEach { state ->
            when (state) {
                is NetworkResponseState.Success -> {
                    val cityData = state.data as List<CityData>
                    if(cityData.isNotEmpty()) {
                        _weatherState.update { it.copy(cityName = cityData[0].name) }
                        getWeatherData(cityData[0].lat, cityData[0].lon, locale)
                    }
                    else _weatherState.update { it.copy(loading = false, hasError = true) }
                }

                else -> {
                    _weatherState.update { it.copy(loading = false, hasError = true) }
                }
            }
        }.catch {
            _weatherState.update { it.copy(loading = false, hasError = true) }
        }.launchIn(viewModelScope)
    }

    fun getWeatherData(lat: Double, lon: Double, locale: Locale) {
        _weatherState.update { it.copy(loading = true, hasError = false) }
        weatherRepository.getWeather(lat, lon, getUnitsFromLocale(getCountryFromLocale(locale))).onEach { state ->
            when (state) {
                is NetworkResponseState.Success -> {
                    val data = state.data as OpenWeatherData
                    if(data.weather.isNotEmpty()) {
                        lateinit var temperature: Temperature
                        data.main.let {
                            temperature = Temperature(
                                units = getUnitsFromLocale(getCountryFromLocale(locale)),
                                temp = it.temp ?: 0.0,
                                minTemp = it.minTemp ?: 0.0,
                                maxTemp = it.maxTemp ?: 0.0,
                                feelsLike = it.feelsLike ?: 0.0,
                                pressure = it.pressure ?: 0.0,
                                humidity = it.humidity ?: 0.0,
                                seaLevel = it.seaLevel ?: 0.0,
                                groundLevel = it.groundLevel ?: 0.0,
                            )
                        }
                        val system = System(
                            date = DateTimeHelper.dateTimeToLocalDate(data.dateTime, locale),
                            sunrise = DateTimeHelper.dateTimeToLocalTime(data.system.sunrise, locale),
                            sunset = DateTimeHelper.dateTimeToLocalTime(data.system.sunset, locale),
                        )
                        _weatherState.update {
                            it.copy(
                                loading = false,
                                weather = data.weather[0],
                                temperature = temperature,
                                system = system,
                                cityName = data.name
                            )
                        }
                    }
                    else _weatherState.update { it.copy(loading = false, hasError = true) }
                }

                else -> {
                    _weatherState.update { it.copy(loading = false, hasError = true) }
                }
            }
        }.catch {
            _weatherState.update { it.copy(loading = false, hasError = true) }
        }.launchIn(viewModelScope)
    }

    private fun getCountryFromLocale(locale: Locale): String {
        return (locale.country.uppercase(Locale.ROOT))
    }

    /**
     * Use temperature units from user's primary locale
     */
    private fun getUnitsFromLocale(country: String): String {
        return when (country.uppercase(Locale.ROOT)) {
            // US, UK, Myanmar, Liberia,
            "US", "GB", "MM", "LR" -> "imperial"
            else -> "metric"
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WeatherViewModel(WeatherRepositoryImpl(), CityDataRepositoryImpl())
            }
        }
    }
}