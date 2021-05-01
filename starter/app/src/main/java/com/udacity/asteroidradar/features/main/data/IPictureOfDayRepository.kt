package com.udacity.asteroidradar.features.main.data

import com.udacity.asteroidradar.features.main.domain.model.PictureOfDay

interface IPictureOfDayRepository {
    suspend fun getPictureOfDay(): PictureOfDay
}
