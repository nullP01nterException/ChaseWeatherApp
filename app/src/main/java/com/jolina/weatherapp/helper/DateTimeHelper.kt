package com.jolina.weatherapp.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * A helper for converting unix timestamp to local time
 */

class DateTimeHelper {
    companion object {
        fun dateTimeToLocalDate(dateTime: Long, locale: Locale): String {
            val date = Date(dateTime * 1000) //time to millis
            val format = SimpleDateFormat("MMMM dd yyyy h:mm a", locale).apply {
                timeZone = TimeZone.getDefault()
            }

            return format.format(date)
        }

        fun dateTimeToLocalTime(dateTime: Long, locale: Locale): String {
            val date = Date(dateTime * 1000)
            val format = SimpleDateFormat("h:mm a", locale).apply {
                timeZone = TimeZone.getDefault()
            }

            return format.format(date)
        }
    }
}