package com.udacity.asteroidradar.data.network

import com.udacity.asteroidradar.common.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidsApisService {
    @GET("neo/rest/v1/feed?api_key=" + API_KEY)
    suspend fun getProperties(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): String
}


