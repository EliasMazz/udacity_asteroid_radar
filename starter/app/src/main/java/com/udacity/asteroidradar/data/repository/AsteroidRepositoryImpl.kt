package com.udacity.asteroidradar.data.repository

import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.asDomainModel
import com.udacity.asteroidradar.data.network.exception.NoNetworkException
import com.udacity.asteroidradar.data.network.models.asDatabaseModel
import com.udacity.asteroidradar.data.network.api.FetchAsteroidsWithTimeRangeAPI
import com.udacity.asteroidradar.features.main.data.IAsteroidRepository
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate

class AsteroidRepositoryImpl(
    private val asteroidDao: AsteroidDao,
    private val fetchAsteroidsWithTimeRangeAPI: FetchAsteroidsWithTimeRangeAPI
) : IAsteroidRepository {
    private val logTag = AsteroidRepositoryImpl::class.java.toString()

    override suspend fun getAllAsteroidList(): List<Asteroid> = withContext(Dispatchers.IO) {
        asteroidDao.getAllAsteroids()
            .map { it.asDomainModel() }
    }

    override suspend fun getTodayAsteroidList(today: LocalDate): List<Asteroid> = withContext(Dispatchers.IO) {
        asteroidDao.getAsteroidsByDate(today)
            .map { it.asDomainModel() }
    }

    override suspend fun getWeekAsteroidList(
        startDay: LocalDate, endDay: LocalDate
    ): List<Asteroid> = withContext(Dispatchers.IO) {
        asteroidDao.getAsteroidsByDateRange(startDay, endDay)
            .map { it.asDomainModel() }
    }

    override suspend fun refreshAsteroidList(): RefreshAsteroidListUseCase.Result = withContext(Dispatchers.IO) {
        try {
            val asteroidListResponse = fetchAsteroidsWithTimeRangeAPI.request()
            asteroidDao.insertAll(
                *asteroidListResponse.map {
                    it.asDatabaseModel()
                }.toTypedArray()
            )
            return@withContext RefreshAsteroidListUseCase.Result.Success
        } catch (e: Exception) {
            Logger.e(logTag, e.toString())
            if (e is NoNetworkException) {
                return@withContext RefreshAsteroidListUseCase.Result.NoInternet
            } else {
                return@withContext RefreshAsteroidListUseCase.Result.GeneralError
            }
        }
    }

}
