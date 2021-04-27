package com.udacity.asteroidradar.main.repository

import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.asViewDataModel
import com.udacity.asteroidradar.data.network.exception.NoNetworkException
import com.udacity.asteroidradar.data.network.models.asDatabaseModel
import com.udacity.asteroidradar.main.domain.FetchAsteroidsUseCase
import com.udacity.asteroidradar.main.model.AsteroidViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate
import java.util.*

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val fetchAsteroidsUseCase: FetchAsteroidsUseCase
) {
    private val logTag = AsteroidRepository::class.java.toString()

    suspend fun getAllAsteroidList(): List<AsteroidViewData> = withContext(Dispatchers.IO) {
        asteroidDao.getAllAsteroids()
            .map { it.asViewDataModel() }
    }

    suspend fun getTodayAsteroidList(): List<AsteroidViewData> = withContext(Dispatchers.IO) {
        asteroidDao.getAsteroidsByDate(LocalDate.now())
            .map { it.asViewDataModel() }
    }

    suspend fun getWeeksteroidList(): List<AsteroidViewData> = withContext(Dispatchers.IO) {
        asteroidDao.getAsteroidsByDateRange(LocalDate.now(), LocalDate.now().plusDays(7))
            .map { it.asViewDataModel() }
    }

    suspend fun refreshAsteroidList(): Result = withContext(Dispatchers.IO) {
        try {
            val asteroidListResponse = fetchAsteroidsUseCase.fetchAsteroidsWithTimeRange()
            asteroidDao.insertAll(
                *asteroidListResponse.map {
                    it.asDatabaseModel()
                }.toTypedArray()
            )
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
