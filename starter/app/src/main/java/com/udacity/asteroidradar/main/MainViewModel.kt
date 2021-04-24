package com.udacity.asteroidradar.main

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.network.Asteroid
import com.udacity.asteroidradar.data.network.PictureOfDay
import com.udacity.asteroidradar.data.network.PictureOfDayApiService
import com.udacity.asteroidradar.data.network.exception.NoNetworkException
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val asteroidRepository: AsteroidRepository,
    private val pictureOfDayApiService: PictureOfDayApiService
) : ViewModel() {

    private val logTag = MainViewModel::class.java.toString()
    val listAsteroid = asteroidRepository.asteroidList

    val refreshAsteroidResult: LiveData<AsteroidRepository.Result>
        get() = _refreshResult
    private val _refreshResult = MutableLiveData<AsteroidRepository.Result>()

    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()

    init {
        refreshAsteroids()
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = pictureOfDayApiService.getPictureOfDay()
            } catch (e: Exception) {
                Logger.e(logTag, e.toString())
            }
        }
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            _refreshResult.value = asteroidRepository.refreshAsteroidList()
        }
    }

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroidDetail.value = null
    }

}
