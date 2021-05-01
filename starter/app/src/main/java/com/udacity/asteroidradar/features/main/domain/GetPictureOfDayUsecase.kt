package com.udacity.asteroidradar.features.main.domain

import com.udacity.asteroidradar.features.main.data.IPictureOfDayRepository
import com.udacity.asteroidradar.features.main.domain.model.asViewData
import com.udacity.asteroidradar.features.main.ui.model.PictureOfDayViewData

class GetPictureOfDayUsecase(
    private val pictureOfDayRepository: IPictureOfDayRepository
) {
    suspend fun invoke(): PictureOfDayViewData? = pictureOfDayRepository.getPictureOfDay().asViewData()
}
