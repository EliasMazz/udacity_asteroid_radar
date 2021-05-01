package com.udacity.asteroidradar.features.main.domain.model

import com.udacity.asteroidradar.common.DateFormat
import com.udacity.asteroidradar.data.db.model.AsteroidEntity
import com.udacity.asteroidradar.features.main.model.AsteroidViewData

data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun Asteroid.asViewData(): AsteroidViewData {
    return AsteroidViewData(
        id = this.id,
        codename = this.codename,
        closeApproachDate = this.closeApproachDate,
        absoluteMagnitude = this.absoluteMagnitude,
        estimatedDiameter = this.estimatedDiameter,
        relativeVelocity = this.relativeVelocity,
        distanceFromEarth = this.distanceFromEarth,
        isPotentiallyHazardous = this.isPotentiallyHazardous
    )
}
