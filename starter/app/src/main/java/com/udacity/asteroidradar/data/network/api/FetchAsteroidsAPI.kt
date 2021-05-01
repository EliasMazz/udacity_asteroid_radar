package com.udacity.asteroidradar.data.network.api

import com.udacity.asteroidradar.common.DateFormat.dateFormat
import com.udacity.asteroidradar.data.network.models.AsteroidResponse
import com.udacity.asteroidradar.data.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.network.service.AsteroidsApisService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate

class FetchAsteroidsAPI(
    private val asteroidsApisService: AsteroidsApisService
) {
    suspend fun fetchAsteroidsWithTimeRange(): List<AsteroidResponse> = withContext(Dispatchers.IO) {
        val startDate = LocalDate.now().format(dateFormat)
        val endDate = LocalDate.now().plusDays(7).format(dateFormat)
        return@withContext parseAsteroidsJsonResult(
            JSONObject
                (
                asteroidsApisService.getProperties(
                    startDate = startDate,
                    endDate = endDate
                )
            )
        ).toList()
    }
}


