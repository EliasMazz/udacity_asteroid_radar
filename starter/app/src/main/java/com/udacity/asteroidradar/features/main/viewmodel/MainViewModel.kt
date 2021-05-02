package com.udacity.asteroidradar.features.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.network.models.PictureOfDayResponse
import com.udacity.asteroidradar.data.network.service.PictureOfDayApiService
import com.udacity.asteroidradar.features.main.ui.model.AsteroidFilterViewData
import com.udacity.asteroidradar.features.main.ui.model.AsteroidViewData
import com.udacity.asteroidradar.features.main.domain.GetAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetFilteredAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetPictureOfDayUsecase
import com.udacity.asteroidradar.features.main.domain.GetTodayAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.GetWeekAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase
import com.udacity.asteroidradar.features.main.domain.RefreshAsteroidListUseCase.*
import com.udacity.asteroidradar.features.main.domain.model.asViewData
import com.udacity.asteroidradar.features.main.ui.model.PictureOfDayViewData
import com.udacity.asteroidradar.features.main.ui.model.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel(
    private val filteredAsteroidListUseCase: GetFilteredAsteroidListUseCase,
    private val refreshAsteroidListUseCase: RefreshAsteroidListUseCase,
    private val getPictureOfDayUsecase: GetPictureOfDayUsecase
) : ViewModel() {

    private val logTag = MainViewModel::class.java.toString()

    val asteroidList: LiveData<List<AsteroidViewData>>
        get() = _asteroidList
    private var _asteroidList = MutableLiveData<List<AsteroidViewData>>()

    val refreshAsteroidResult: LiveData<Result>
        get() = _refreshResult
    private val _refreshResult = MutableLiveData<Result>()

    val pictureOfDay: LiveData<PictureOfDayViewData?>
        get() = _pictureOfDay
    private val _pictureOfDay = MutableLiveData<PictureOfDayViewData?>()

    private val filter: AsteroidFilterViewData = AsteroidFilterViewData.SAVED

    init {
        filterAsteroidList(filter)
        refreshAsteroids()
        getPictureOfDay()
    }

    fun filterAsteroidList(filter: AsteroidFilterViewData) {
        viewModelScope.launch {
            try {
                val filteredAsteroidList = filteredAsteroidListUseCase.invoke(filter.asDomain())
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
            try {
                _refreshResult.value = refreshAsteroidListUseCase.invoke()
                filterAsteroidList(filter)
                onAsteroidRefreshed()
            } catch (e: Exception) {
                Logger.e(logTag, e.toString())
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = getPictureOfDayUsecase.invoke()
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
