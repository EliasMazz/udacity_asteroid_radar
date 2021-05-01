package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.network.api.FetchPictureOfDayAPI
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse
import com.udacity.asteroidradar.data.network.models.asDomainModel
import com.udacity.asteroidradar.features.main.data.IAsteroidRepository
import com.udacity.asteroidradar.features.main.data.IPictureOfDayRepository
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import com.udacity.asteroidradar.features.main.domain.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PictureOfDayRepositoryImpl(
    private val fetchPictureOfDayAPI: FetchPictureOfDayAPI
) : IPictureOfDayRepository {
    override suspend fun getPictureOfDay(): PictureOfDay = withContext(Dispatchers.IO) {
        fetchPictureOfDayAPI.request().asDomainModel()
    }
}
