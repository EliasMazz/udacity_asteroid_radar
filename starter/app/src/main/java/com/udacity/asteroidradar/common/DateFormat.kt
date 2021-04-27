package com.udacity.asteroidradar.common

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormat {
    val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
}
