package com.jolina.weatherapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CityData(
    @Json(name = "name") val name: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double
) : Parcelable