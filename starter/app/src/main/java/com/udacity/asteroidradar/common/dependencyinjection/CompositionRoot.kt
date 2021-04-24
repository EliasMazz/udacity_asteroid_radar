package com.udacity.asteroidradar.common.dependencyinjection

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.common.Constants
import com.udacity.asteroidradar.data.network.PictureOfDayApiService
import com.udacity.asteroidradar.data.network.AsteroidsApisService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class CompositionRoot {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    val asteroidService: AsteroidsApisService by lazy {
        retrofit.create(AsteroidsApisService::class.java)
    }

    val pictureOfDayService: PictureOfDayApiService by lazy {
        retrofit.create(PictureOfDayApiService::class.java)
    }
}


