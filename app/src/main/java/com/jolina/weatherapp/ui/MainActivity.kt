package com.jolina.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jolina.weatherapp.R
import com.jolina.weatherapp.databinding.ActivityMainBinding

/**
 * Entry point for the app
 * Locked orientation to portrait in the Manifest as the UI built out looks better in portrait
 * Given more time, it would be nice to build out a separate UI for landscape use
 */

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.container, WeatherFragment.newInstance())
            .commitAllowingStateLoss()
    }

    /**
     * Set the binding to null when destroying to prevent memory leaks
     * This is also done in the fragment
     */
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}