package com.udacity.asteroidradar.features.main.domain

import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.data.IAsteroidRepository
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshAsteroidListUseCase(
    private val asteroidRepository: IAsteroidRepository
) {
    suspend fun invoke(): Result = withContext(Dispatchers.IO) {
        asteroidRepository.refreshAsteroidList()
    }

    sealed class Result {
        object Success : Result()
        object NoInternet : Result()
        object GeneralError : Result()
    }
}
