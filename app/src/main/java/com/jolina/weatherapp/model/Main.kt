package com.jolina.weatherapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp") val temp: Double?,
    @Json(name = "feels_like") val feelsLike: Double?,
    @Json(name = "temp_min") val minTemp: Double?,
    @Json(name = "temp_max") val maxTemp: Double?,
    @Json(name = "pressure") val pressure: Double?,
    @Json(name = "humidity") val humidity: Double?,
    @Json(name = "sea_level") val seaLevel: Double?,
    @Json(name = "grnd_level") val groundLevel: Double?,
) : Parcelable
