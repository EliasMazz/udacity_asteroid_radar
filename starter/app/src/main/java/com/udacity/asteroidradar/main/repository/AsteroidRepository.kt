package com.udacity.asteroidradar.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.asDatabaseModel
import com.udacity.asteroidradar.data.db.model.asViewDataModel
import com.udacity.asteroidradar.data.network.exception.NoNetworkException
import com.udacity.asteroidradar.main.domain.FetchAsteroidsUseCase
import com.udacity.asteroidradar.main.model.AsteroidViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val fetchAsteroidsUseCase: FetchAsteroidsUseCase
) {
    private val logTag = AsteroidRepository::class.java.toString()

    val asteroidList: LiveData<List<AsteroidViewData>> =
        Transformations.map(asteroidDao.getAllAsteroids()) {
        it.asViewDataModel()
    }

    suspend fun refreshAsteroidList(): Result = withContext(Dispatchers.IO) {
        try {
            val asteroidListResponse = fetchAsteroidsUseCase.fetchAsteroidsWithTimeRange()
            asteroidDao.insertAll(*asteroidListResponse.asDatabaseModel())
            return@withContext Result.Success
        } catch (e: Exception) {
            Logger.e(logTag, e.toString())
            if (e is NoNetworkException) {
                return@withContext Result.NoInternet
            } else {
                return@withContext Result.GeneralError
            }
        }
    }

    sealed class Result {
        object Success : Result()
        object NoInternet : Result()
        object GeneralError : Result()
    }
}
