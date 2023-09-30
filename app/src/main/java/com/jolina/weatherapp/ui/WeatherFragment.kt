package com.jolina.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.jolina.weatherapp.databinding.FragmentWeatherBinding
import com.jolina.weatherapp.helper.KeyboardHelper
import com.jolina.weatherapp.model.Weather
import com.jolina.weatherapp.model.local.System
import com.jolina.weatherapp.model.local.Temperature
import com.jolina.weatherapp.viewmodel.WeatherViewModel
import com.jolina.weatherapp.viewmodel.WeatherViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

/**
 * Fragment that allows the user to search for cities and displays the weather
 *
 * Location Permission:
 * Requests user for permission to use coarse location to get weather in their current city
 * If the user denies, they have a chance to change their mind as a UI that takes
 * them near the app permission screen appears
 * This UI only appears if there is no cached search data
 *
 * Caching Recent Search:
 * Saves data from the user's last successful search in SharedPreferences to automatically
 * populate UI when user returns to app
 * Given more time it would be better practice to but this data in a Room or Realm as space in
 * SharedPreferences is limited and the amount of data needed to be saved could grow in the future
 */

class WeatherFragment : Fragment() {

    // databinded to fragment_weather.xml for the xml to figure out how to display the data
    val viewState: ObservableField<WeatherViewState> = ObservableField(WeatherViewState())

    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }

    /**
     * if user allows location permission, make a search using their latitude and longitude
     * else show UI that lets users allow permission if they change their mind
     */
    private val permissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.requestPermissionLayout.visibility = View.GONE
                getCurrentLocationWeather()
            } else {
                binding.requestPermissionLayout.visibility = View.VISIBLE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.fragment = this
        setupEditText()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.weatherState.onEach { state ->
            binding.requestPermissionLayout.visibility = View.GONE

            // updates the state bound to the xml so the UI shows the most current state
            viewState.set(state)

            if(state.wasSearched) saveToSharedPrefs(state)
        }.launchIn(lifecycleScope)
    }

    /**
     * After all the views are created check if there was a recent search in SharedPreferences
     * If there is, retrieve and display data from last search
     * Else, check if the user has given the app permission to load the weather from their
     * current location
     */
    override fun onStart() {
        super.onStart()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        if (!sharedPref.getString(LAST_SEARCHED_CITY_NAME, null).isNullOrBlank()) {
            retrieveFromSharedPrefs()
        } else {
            checkLocationPermission()
        }
    }

    fun onSearchClicked(v: View) {
        // hide keyboard after searching
        activity?.let { act ->
            KeyboardHelper.hideKeyboard(act, binding.root)
        }

        val input = binding.searchEditText.text.toString()
        if (input.isNotBlank()) {
            viewModel.getLocationData(input, getLocale())
        }
    }

    /**
     * If user denied location access permission and changes their mind, take them to app details
     * page in Settings since the permission dialog won't pop up again in the current session
     */
    fun onPermissionClicked(v: View) {
        activity?.let {
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val uri = Uri.fromParts("package", it.packageName, null)
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = uri
                }
                startActivity(intent)
            }
        }
    }

    // set to null to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // use the user's primary locale for formatting data in the viewModel
    private fun getLocale(): Locale {
        return resources.configuration.locales[0]
    }

    // if the user allows permission and doesn't have cached search data get current location weather
    private fun checkLocationPermission() {
        activity?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {
                    binding.requestPermissionLayout.visibility = View.GONE
                    getCurrentLocationWeather()
                }

                else -> {
                    permissionRequestLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }

    /**
     * Only called from [checkLocationPermission] which already checks for permission
     * Needed to suppress or it would check for permission again in this function
     */
    @SuppressLint("MissingPermission")
    private fun getCurrentLocationWeather() {
        activity?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.getBestProvider(Criteria(), false)?.let { provider ->
                locationManager.getLastKnownLocation(provider)?.let { location ->
                    viewModel.getWeatherData(
                        location.latitude,
                        location.longitude,
                        getLocale()
                    )
                }
            }
        }
    }

    // intercepts the enter key typed from device keyboard to search instead of going to new line
    private fun setupEditText() {
        binding.searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                onSearchClicked(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    // saves successful search to SharedPreferences
    private fun saveToSharedPrefs(viewState: WeatherViewState) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val gson = Gson()

        with(sharedPref.edit()) {
            putString(LAST_SEARCHED_CITY_NAME, viewState.cityName)
            putString(LAST_SEARCHED_WEATHER, gson.toJson(viewState.weather))
            putString(LAST_SEARCHED_TEMPERATURE, gson.toJson(viewState.temperature))
            putString(LAST_SEARCHED_SYSTEM, gson.toJson(viewState.system))
            apply()
        }
    }

    // gets all data from last successful search to populate UI without making API call (works offline)
    private fun retrieveFromSharedPrefs() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val gson = Gson()

        val cityName = sharedPref.getString(LAST_SEARCHED_CITY_NAME, "")
        val weather = sharedPref.getString(LAST_SEARCHED_WEATHER, null)
        val temperature = sharedPref.getString(LAST_SEARCHED_TEMPERATURE, null)
        val system = sharedPref.getString(LAST_SEARCHED_SYSTEM, null)
        if(weather == null || temperature == null || system == null) {
            viewModel.getLocationData(cityName!!, getLocale())
        } else {
            viewState.set(
                WeatherViewState(
                    cityName = cityName ?: "",
                    weather = gson.fromJson(weather, Weather::class.java),
                    temperature = gson.fromJson(temperature, Temperature::class.java),
                    system = gson.fromJson(system, System::class.java)
                )
            )
        }
    }

    companion object {
        //SharedPreferences keys
        private const val LAST_SEARCHED_CITY_NAME = "LAST_SEARCHED_CITY_NAME"
        private const val LAST_SEARCHED_WEATHER = "LAST_SEARCHED_WEATHER"
        private const val LAST_SEARCHED_TEMPERATURE = "LAST_SEARCHED_TEMPERATURE"
        private const val LAST_SEARCHED_SYSTEM = "LAST_SEARCHED_SYSTEM"

        fun newInstance(): WeatherFragment {
            return WeatherFragment()
        }
    }
}