package com.jolina.weatherapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "sunrise") val sunrise: Long,
    @Json(name = "sunset") val sunset: Long
) : Parcelable