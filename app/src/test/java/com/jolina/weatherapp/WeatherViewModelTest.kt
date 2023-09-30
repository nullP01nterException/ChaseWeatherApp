package com.jolina.weatherapp

import com.jolina.weatherapp.model.TestNetworkState
import com.jolina.weatherapp.repository.FakeCityDataRepositoryImpl
import com.jolina.weatherapp.repository.FakeWeatherRepositoryImpl
import com.jolina.weatherapp.rule.MainDispatcherRule
import com.jolina.weatherapp.viewmodel.WeatherViewModel
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.util.Locale

class WeatherViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val locale = Locale.ENGLISH

    private lateinit var weatherViewModel: WeatherViewModel

    @Test
    fun assertLoadingIsTrueOnFirstEmissionFromLocationRepoCall() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.LOADING)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.LOADING)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getLocationData("Anaheim", locale)

        assert(weatherViewModel.weatherState.first().loading)
    }

    @Test
    fun assertLoadingIsTrueOnFirstEmissionFromWeatherRepoCall() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.LOADING)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.LOADING)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getWeatherData(10.0,20.0, locale)

        assert(weatherViewModel.weatherState.first().loading)
    }

    @Test
    fun assertHasErrorIsTrueWhenErrorFromLocationRepoCall() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.LOADING)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.ERROR)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getLocationData("Anaheim", locale)

        assert(weatherViewModel.weatherState.first().hasError)
    }

    @Test
    fun assertHasErrorIsTrueWhenErrorFromWeatherRepoCall() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.ERROR)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.LOADING)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getWeatherData(10.0,20.0, locale)

        assert(weatherViewModel.weatherState.first().hasError)
    }

    @Test
    fun assertHasErrorIsTrueWhenLocationRepoHasEmptyList() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.LOADING)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.SUCCESS, 0)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getLocationData("Anaheim", locale)

        assert(weatherViewModel.weatherState.first().hasError)
    }

    @Test
    fun assertHasErrorIsTrueWhenWeatherRepoHasEmptyList() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.SUCCESS, 0)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.LOADING)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getWeatherData(10.0,20.0, locale)

        assert(weatherViewModel.weatherState.first().hasError)
    }

    @Test
    fun assertSuccessWhenLocationRepoReturnsAllData() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.LOADING)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.SUCCESS)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getLocationData("Anaheim", locale)

        assert(weatherViewModel.weatherState.first().cityName.isNotBlank())
    }

    @Test
    fun assertSuccessWhenWeatherRepoReturnsAllData() = runBlocking {
        val fakeWeatherRepo =  FakeWeatherRepositoryImpl(TestNetworkState.SUCCESS)
        val fakeCityDataRepo = FakeCityDataRepositoryImpl(TestNetworkState.LOADING)
        weatherViewModel = WeatherViewModel(fakeWeatherRepo, fakeCityDataRepo)
        weatherViewModel.getWeatherData(10.0,20.0, locale)

        assertNotNull(weatherViewModel.weatherState.first().weather)
    }
}