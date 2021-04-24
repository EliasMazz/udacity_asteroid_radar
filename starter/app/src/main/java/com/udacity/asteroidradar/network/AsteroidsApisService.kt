package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.common.Constants.API_KEY
import com.udacity.asteroidradar.common.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidsApisService {
    @GET("neo/rest/v1/feed?api_key=" + API_KEY)
    suspend fun getProperties(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): String
}


