package com.jolina.weatherapp.ui.bindingAdapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jolina.weatherapp.R
import kotlin.math.ceil
import com.jolina.weatherapp.model.local.Temperature

/**
 * Binding adapters to format data from local model [Temperature]
 */

@BindingAdapter("android:temperature")
fun setTemperature(view: TextView, temperature: Double) {
    view.text = ceil(temperature).toInt().toString()
}

@BindingAdapter("android:feelsLike")
fun setFeelsLike(view: TextView, feelsLikeTemp: Double) {
    val temp = ceil(feelsLikeTemp).toInt().toString()
    view.text = view.context.getString(R.string.feels_like, temp)
}

@BindingAdapter(value = ["android:high", "android:low"], requireAll = true)
fun setHighLow(view: TextView, high: Double, low: Double) {
    val highTemp = ceil(high).toInt().toString()
    val lowTemp = ceil(low).toInt().toString()
    view.text = view.context.getString(R.string.high_low, highTemp, lowTemp)
}

@BindingAdapter("android:pressure")
fun setPressure(view: TextView, pressure: Double) {
    val pres = ceil(pressure).toInt().toString()
    view.text = view.context.getString(R.string.pressure_unit, pres)
}

@BindingAdapter("android:humidity")
fun setHumidity(view: TextView, humidity: Double) {
    val hum = ceil(humidity).toInt().toString()
    view.text = view.context.getString(R.string.humidity_unit, hum)
}