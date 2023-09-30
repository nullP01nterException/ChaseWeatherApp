package com.jolina.weatherapp.ui.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Binding adapter for images that uses Glide to asynchronously load
 */

@BindingAdapter("android:icon")
fun setWeatherIcon(view: ImageView, icon: String?){
    if(!icon.isNullOrBlank()) {
        Glide.with(view.context).load("https://openweathermap.org/img/wn/$icon@2x.png").into(view)
    }
}