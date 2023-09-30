package com.jolina.weatherapp.ui.bindingAdapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jolina.weatherapp.R
import com.jolina.weatherapp.model.local.System

/**
 * Binding adapters to format data from local model [System]
 */

@BindingAdapter("android:sunrise")
fun setSunrise(view: TextView, time: String?) {
    if(!time.isNullOrBlank()) {
        view.text = view.context.getString(R.string.sunrise, time)
    }
}

@BindingAdapter("android:sunset")
fun setSunset(view: TextView, time: String?) {
    if(!time.isNullOrBlank()) {
        view.text = view.context.getString(R.string.sunset, time)
    }
}