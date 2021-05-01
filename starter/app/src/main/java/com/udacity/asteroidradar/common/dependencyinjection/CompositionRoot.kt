package com.udacity.asteroidradar.common.dependencyinjection

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.common.Constants
import com.udacity.asteroidradar.common.Constants.ASTEROID_RADAR_DATABASE
import com.udacity.asteroidradar.common.Constants.CONNECT_TIMEOUT_TIME
import com.udacity.asteroidradar.common.Constants.READ_TIMEOUT_TIME
import com.udacity.asteroidradar.data.db.AsteroidDataBase
import com.udacity.asteroidradar.data.network.service.PictureOfDayApiService
import com.udacity.asteroidradar.data.network.service.AsteroidsApisService
import com.udacity.asteroidradar.data.network.interceptor.RequestInterceptor
import com.udacity.asteroidradar.data.network.api.FetchAsteroidsAPI
import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.data.IAsteroidRepository
import com.udacity.asteroidradar.features.main.domain.GetAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetTodayAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetWeekAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

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
            .readTimeout(READ_TIMEOUT_TIME, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
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

    private val asteroidService: AsteroidsApisService by lazy {
        retrofit.create(AsteroidsApisService::class.java)
    }

    private val fetchAsteroidsAPI: FetchAsteroidsAPI = FetchAsteroidsAPI(asteroidService)

    val pictureOfDayService: PictureOfDayApiService by lazy {
        retrofit.create(PictureOfDayApiService::class.java)
    }

    private val database by lazy {
        Room.databaseBuilder(
            application,
            AsteroidDataBase::class.java,
            ASTEROID_RADAR_DATABASE
        ).build()
    }

    private val asteroidRepository: IAsteroidRepository by lazy {
        AsteroidRepositoryImpl(
            database.asteroidDao(),
            fetchAsteroidsAPI
        )
    }

    val getAsteroidListUseCase = GetAsteroidListUseCase(asteroidRepository)

    val getTodayAsteroidListUseCase = GetTodayAsteroidListUseCase(asteroidRepository)

    val getWeekAsteroidListUseCase = GetWeekAsteroidListUseCase(asteroidRepository)

    val refreshAsteroidListUseCase = RefreshAsteroidListUseCase(asteroidRepository)
}


