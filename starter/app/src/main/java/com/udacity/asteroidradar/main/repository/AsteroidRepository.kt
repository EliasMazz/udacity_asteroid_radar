package com.udacity.asteroidradar.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.asDatabaseModel
import com.udacity.asteroidradar.data.db.model.asDomainModel
import com.udacity.asteroidradar.data.network.AsteroidsApisService
import com.udacity.asteroidradar.data.network.exception.NoNetworkException
import com.udacity.asteroidradar.data.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.main.model.AsteroidFormatRequest
import com.udacity.asteroidradar.main.model.AsteroidViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val asteroidsApisService: AsteroidsApisService
) {
    private val logTag = AsteroidRepository::class.java.toString()

    val asteroidList: LiveData<List<AsteroidViewData>> = Transformations.map(asteroidDao.getAllAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroidList(): Result = withContext(Dispatchers.IO) {
        try {
            val asteroidList = parseAsteroidsJsonResult(
                JSONObject(
                    asteroidsApisService.getProperties(
                        AsteroidFormatRequest.getFormattedDate(Calendar.getInstance()),
                        AsteroidFormatRequest.getFormattedDate(Calendar.getInstance().apply {
                            add(Calendar.DATE, 7)
                        })
                    )
                )
            ).toList()
            asteroidDao.insertAll(*asteroidList.asDatabaseModel())

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
