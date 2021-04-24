package com.udacity.asteroidradar.main

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object AsteroidFormatRequest {
    fun getFormattedDate(calendar: Calendar): String = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
}

