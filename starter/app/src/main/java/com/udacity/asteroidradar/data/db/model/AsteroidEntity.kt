package com.udacity.asteroidradar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.common.DateFormat.dateFormat
import com.udacity.asteroidradar.features.main.domain.model.Asteroid
import java.time.LocalDate

@Entity
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: LocalDate?,
    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
)

fun AsteroidEntity.asDomainModel(): Asteroid {
    return Asteroid(
            id = this.id,
            codename = this.codename,
            closeApproachDate = if (this.closeApproachDate == null) "" else this.closeApproachDate.format(dateFormat),
            absoluteMagnitude = this.absoluteMagnitude,
            estimatedDiameter = this.estimatedDiameter,
            relativeVelocity = this.relativeVelocity,
            distanceFromEarth = this.distanceFromEarth,
            isPotentiallyHazardous = this.isPotentiallyHazardous
        )
}

