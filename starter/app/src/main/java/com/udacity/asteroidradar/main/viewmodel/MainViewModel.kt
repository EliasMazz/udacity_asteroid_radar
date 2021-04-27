package com.udacity.asteroidradar.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.network.models.PictureOfDay
import com.udacity.asteroidradar.data.network.service.PictureOfDayApiService
import com.udacity.asteroidradar.main.model.AsteroidFilterViewData
import com.udacity.asteroidradar.main.model.AsteroidViewData
import com.udacity.asteroidradar.main.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel(
    private val asteroidRepository: AsteroidRepository,
    private val pictureOfDayApiService: PictureOfDayApiService
) : ViewModel() {

    private val logTag = MainViewModel::class.java.toString()

    val asteroidList: LiveData<List<AsteroidViewData>>
        get() = _asteroidList
    private var _asteroidList = MutableLiveData<List<AsteroidViewData>>()

    val refreshAsteroidResult: LiveData<AsteroidRepository.Result>
        get() = _refreshResult
    private val _refreshResult = MutableLiveData<AsteroidRepository.Result>()

    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()

    private val filter: AsteroidFilterViewData = AsteroidFilterViewData.SAVED

    init {
        filterAsteroidList(filter)
        refreshAsteroids()
        getPictureOfDay()
    }

    fun filterAsteroidList(filter: AsteroidFilterViewData) {
        viewModelScope.launch {
            try {
                val filteredAsteroidList = when (filter) {
                    AsteroidFilterViewData.SAVED -> asteroidRepository.getAllAsteroidList()
                    AsteroidFilterViewData.TODAY -> asteroidRepository.getTodayAsteroidList()
                    AsteroidFilterViewData.WEEK -> asteroidRepository.getWeeksteroidList()
                }
                withContext(Dispatchers.Main.immediate) {
                    _asteroidList.postValue(filteredAsteroidList)
                }
            } catch (e: Exception) {
                Logger.e(logTag, e.toString())
            }
        }
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
            filterAsteroidList(filter)
            onAsteroidRefreshed()
        }
    }

    private fun onAsteroidRefreshed() {
        _refreshResult.value = null
    }

    private val _navigateToAsteroidDetail = MutableLiveData<AsteroidViewData?>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    fun onAsteroidClicked(asteroid: AsteroidViewData) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroidDetail.value = null
    }
}
