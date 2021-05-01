package com.udacity.asteroidradar.data.network.service

import com.udacity.asteroidradar.common.Constants
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse
import retrofit2.http.GET

interface PictureOfDayApiService {
    @GET("/planetary/apod?api_key=" + Constants.API_KEY)
    suspend fun getPictureOfDay(): PictureOfDayResponse
}
