package com.udacity.asteroidradar.data.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AsteroidResponse(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)




