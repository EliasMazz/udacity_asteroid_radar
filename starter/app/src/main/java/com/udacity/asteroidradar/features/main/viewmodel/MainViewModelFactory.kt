/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.udacity.asteroidradar.features.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.data.network.service.PictureOfDayApiService
import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.domain.GetAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetTodayAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetWeekAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase

/**
 * Simple ViewModel factory that provides the MarsProperty and context to the ViewModel.
 */
class MainViewModelFactory(
    private val getAsteroidListUseCase: GetAsteroidListUseCase,
    private val getTodayAsteroidListUseCase: GetTodayAsteroidListUseCase,
    private val getWeekAsteroidListUseCase: GetWeekAsteroidListUseCase,
    private val refreshAsteroidListUseCase: RefreshAsteroidListUseCase,
    private val pictureOfDayApiService: PictureOfDayApiService
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                getAsteroidListUseCase,
                getTodayAsteroidListUseCase,
                getWeekAsteroidListUseCase,
                refreshAsteroidListUseCase,
                pictureOfDayApiService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
