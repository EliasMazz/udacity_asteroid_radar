package com.udacity.asteroidradar.data.network

import com.udacity.asteroidradar.common.Constants
import retrofit2.http.GET

interface PictureOfDayApiService {
    @GET("/planetary/apod?api_key=" + Constants.API_KEY)
    suspend fun getPictureOfDay(): PictureOfDay
}
