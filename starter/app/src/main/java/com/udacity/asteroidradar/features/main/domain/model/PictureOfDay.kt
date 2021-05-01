package com.udacity.asteroidradar.features.main.domain.model

import com.squareup.moshi.Json
import com.udacity.asteroidradar.features.main.ui.model.PictureOfDayViewData

data class PictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String
)

fun PictureOfDay.asViewData(): PictureOfDayViewData? =
    if (mediaType != "image") {
        null
    } else {
        PictureOfDayViewData(
            url = this.url,
            title = this.title
        )
    }

