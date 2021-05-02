package com.udacity.asteroidradar.features.main.data

import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase.Result
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import java.time.LocalDate

interface IAsteroidRepository {
    suspend fun getAllAsteroidList(): List<Asteroid>
    suspend fun getAsteroidListByDate(date: LocalDate): List<Asteroid>
    suspend fun getAsteroidListByDateRange(startDay: LocalDate, endDay: LocalDate): List<Asteroid>
    suspend fun refreshAsteroidList(): Result
}
