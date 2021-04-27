package com.udacity.asteroidradar.data.network.models

import android.os.Parcelable
import com.udacity.asteroidradar.common.DateFormat
import com.udacity.asteroidradar.common.DateFormat.dateFormat
import com.udacity.asteroidradar.data.db.model.AsteroidEntity
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

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

fun AsteroidResponse.asDatabaseModel(): AsteroidEntity {
    return AsteroidEntity(
        id = this.id,
        codename = this.codename,
        closeApproachDate = LocalDate.parse(this.closeApproachDate),
        absoluteMagnitude = this.absoluteMagnitude,
        estimatedDiameter = this.estimatedDiameter,
        relativeVelocity = this.relativeVelocity,
        distanceFromEarth = this.distanceFromEarth,
        isPotentiallyHazardous = this.isPotentiallyHazardous
    )
}




