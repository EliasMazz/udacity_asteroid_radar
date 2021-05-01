package com.udacity.asteroidradar.features.main.domain

import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.data.IAsteroidRepository
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class GetTodayAsteroidListUseCase(
    private val asteroidRepository: IAsteroidRepository
) {
    suspend fun invoke(): List<Asteroid> = withContext(Dispatchers.IO) {
        asteroidRepository.getTodayAsteroidList(LocalDate.now())
    }
}
