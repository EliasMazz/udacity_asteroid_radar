package com.udacity.asteroidradar.features.main.domain

import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.data.IAsteroidRepository
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import com.udacity.asteroidradar.features.main.domain.model.AsteroidFilter
import com.udacity.asteroidradar.features.main.ui.model.AsteroidFilterViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFilteredAsteroidListUseCase(
    private val getAsteroidListUseCase: GetAsteroidListUseCase,
    private val getTodayAsteroidListUseCase: GetTodayAsteroidListUseCase,
    private val getWeekAsteroidListUseCase: GetWeekAsteroidListUseCase
) {
    suspend fun invoke(filter: AsteroidFilter): List<Asteroid> = withContext(Dispatchers.IO) {
        return@withContext when (filter) {
            AsteroidFilter.SAVED -> getAsteroidListUseCase.invoke()
            AsteroidFilter.TODAY -> getTodayAsteroidListUseCase.invoke()
            AsteroidFilter.WEEK -> getWeekAsteroidListUseCase.invoke()
        }
    }
}
