package com.udacity.asteroidradar.data.network.models

import com.squareup.moshi.Json
import com.udacity.asteroidradar.features.main.domain.model.PictureOfDay

data class PictureOfDayResponse(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)

fun PictureOfDayResponse.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}
