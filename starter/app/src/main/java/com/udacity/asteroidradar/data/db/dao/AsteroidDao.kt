package com.udacity.asteroidradar.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.db.model.AsteroidEntity
import java.time.LocalDate
import java.util.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("SELECT * FROM asteroidentity")
    suspend fun getAllAsteroids(): List<AsteroidEntity>

    @Query("SELECT * FROM asteroidentity WHERE close_approach_date = :day")
    suspend fun getAsteroidsByDate(day: LocalDate): List<AsteroidEntity>

    @Query("SELECT * FROM asteroidentity WHERE close_approach_date BETWEEN :startDate AND :endDate")
    suspend fun getAsteroidsByDateRange(startDate: LocalDate, endDate: LocalDate): List<AsteroidEntity>

}
