package com.jolina.weatherapp.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * A helper to close the keyboard after user is done searching
 */

class KeyboardHelper {
    companion object {
        fun hideKeyboard(activity: Activity, view: View){
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}