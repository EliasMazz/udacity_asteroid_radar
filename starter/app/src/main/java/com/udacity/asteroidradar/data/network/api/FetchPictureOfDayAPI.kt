package com.udacity.asteroidradar.data.network.api

import com.udacity.asteroidradar.common.DateFormat.dateFormat
import com.udacity.asteroidradar.data.network.models.AsteroidResponse
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse
import com.udacity.asteroidradar.data.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.network.service.AsteroidsApisService
import com.udacity.asteroidradar.data.network.service.PictureOfDayApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate

class FetchPictureOfDayAPI(
    private val service: PictureOfDayApiService
) {
    suspend fun request(): PictureOfDayResponse = withContext(Dispatchers.IO) {
        service.getPictureOfDay()
    }
}


