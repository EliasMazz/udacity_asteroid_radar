package com.udacity.asteroidradar.main.domain

import com.udacity.asteroidradar.data.network.models.AsteroidResponse
import com.udacity.asteroidradar.data.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.network.service.AsteroidsApisService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class FetchAsteroidsUseCase(
    private val asteroidsApisService: AsteroidsApisService
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    suspend fun fetchAsteroidsWithTimeRange(): List<AsteroidResponse> = withContext(Dispatchers.IO) {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance().apply { add(Calendar.DATE, 7) }
        return@withContext parseAsteroidsJsonResult(
            JSONObject
                (
                asteroidsApisService.getProperties(
                    startDate = dateFormat.format(startDate),
                    endDate = dateFormat.format(endDate)
                )
            )
        ).toList()
    }
}


