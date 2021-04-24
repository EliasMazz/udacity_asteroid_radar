package com.udacity.asteroidradar.common.dependencyinjection

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.MyApplication
import com.udacity.asteroidradar.common.Constants
import com.udacity.asteroidradar.data.network.PictureOfDayApiService
import com.udacity.asteroidradar.data.network.AsteroidsApisService
import com.udacity.asteroidradar.data.network.interceptor.RequestInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.AccessControlContext

class CompositionRoot(application: Application) {

    private val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private fun provideHttpClient(
        interceptor: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(requestInterceptor)
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(interceptor)
        }
        return clientBuilder.build()
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(
                provideHttpClient(
                    httpLoggingInterceptor,
                    RequestInterceptor(connectivityManager)
                )
            )
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


