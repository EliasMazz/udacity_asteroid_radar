package com.udacity.asteroidradar.features.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse
import com.udacity.asteroidradar.data.network.service.PictureOfDayApiService
import com.udacity.asteroidradar.features.main.model.AsteroidFilterViewData
import com.udacity.asteroidradar.features.main.model.AsteroidViewData
import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.features.main.domain.GetAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetTodayAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetWeekAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase.*
import com.udacity.asteroidradar.features.main.domain.model.asViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel(
    private val getAsteroidListUseCase: GetAsteroidListUseCase,
    private val getTodayAsteroidListUseCase: GetTodayAsteroidListUseCase,
    private val getWeekAsteroidListUseCase: GetWeekAsteroidListUseCase,
    private val refreshAsteroidListUseCase: RefreshAsteroidListUseCase,
    private val pictureOfDayApiService: PictureOfDayApiService
) : ViewModel() {

    private val logTag = MainViewModel::class.java.toString()

    val asteroidList: LiveData<List<AsteroidViewData>>
        get() = _asteroidList
    private var _asteroidList = MutableLiveData<List<AsteroidViewData>>()

    val refreshAsteroidResult: LiveData<Result>
        get() = _refreshResult
    private val _refreshResult = MutableLiveData<Result>()

    val pictureOfDayResponse: LiveData<PictureOfDayResponse>
        get() = _pictureOfDay
    private val _pictureOfDay = MutableLiveData<PictureOfDayResponse>()

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
                    AsteroidFilterViewData.SAVED -> getAsteroidListUseCase.invoke()
                    AsteroidFilterViewData.TODAY -> getTodayAsteroidListUseCase.invoke()
                    AsteroidFilterViewData.WEEK -> getWeekAsteroidListUseCase.invoke()
                }
                withContext(Dispatchers.Main.immediate) {
                    _asteroidList.postValue(filteredAsteroidList.map {
                        it.asViewData()
                    })
                }
            } catch (e: Exception) {
                Logger.e(logTag, e.toString())
            }
        }
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            _refreshResult.value = refreshAsteroidListUseCase.invoke()
            filterAsteroidList(filter)
            onAsteroidRefreshed()
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
