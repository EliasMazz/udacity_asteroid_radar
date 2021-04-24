package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.network.Asteroid
import com.udacity.asteroidradar.data.network.AsteroidsApisService
import com.udacity.asteroidradar.data.network.PictureOfDay
import com.udacity.asteroidradar.data.network.PictureOfDayApiService
import com.udacity.asteroidradar.data.network.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class MainViewModel(
    private val asteroidsApisService: AsteroidsApisService,
    private val pictureOfDayApiService: PictureOfDayApiService
) : ViewModel() {

    private val _listAsteroid = MutableLiveData<List<Asteroid>>()
    val listAsteroid: LiveData<List<Asteroid>>
        get() = _listAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        getAsteroidsProperties()
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = pictureOfDayApiService.getPictureOfDay()
            } catch (e: Exception) {
                Log.e("ERROR fetching getPictureOfDay ", "" + e)
            }
        }
    }

    private fun getAsteroidsProperties() {
        viewModelScope.launch {

            try {
                _listAsteroid.value = parseAsteroidsJsonResult(
                    JSONObject(
                        asteroidsApisService.getProperties(
                            AsteroidFormatRequest.getFormattedDate(Calendar.getInstance()),
                            AsteroidFormatRequest.getFormattedDate(Calendar.getInstance().apply {
                                add(Calendar.DATE, 7)
                            })
                        )
                    )
                ).toList()
            } catch (e: Exception) {
                Log.e("ERROR fetching getAsteroidsProperties", "" + e)
            }
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
