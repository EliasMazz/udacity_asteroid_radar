package com.udacity.asteroidradar.features.main.ui.model

import com.udacity.asteroidradar.features.main.domain.model.AsteroidFilter
import com.udacity.asteroidradar.features.main.ui.model.AsteroidFilterViewData.*

enum class AsteroidFilterViewData {
    TODAY,
    WEEK,
    SAVED
}

fun AsteroidFilterViewData.asDomain(): AsteroidFilter {
    return when (valueOf(this.name)) {
        TODAY -> AsteroidFilter.valueOf(this.name)
        WEEK -> AsteroidFilter.valueOf(this.name)
        SAVED -> AsteroidFilter.valueOf(this.name)
    }
}


