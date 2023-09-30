package com.jolina.weatherapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class OpenWeatherData(
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "main") val main: Main,
    @Json(name = "dt") val dateTime: Long,
    @Json(name = "sys") val system: Sys,
    @Json(name = "name") val name: String,
) : Parcelable
