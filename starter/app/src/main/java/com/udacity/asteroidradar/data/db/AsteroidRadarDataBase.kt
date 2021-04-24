package com.udacity.asteroidradar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.AsteroidEntity

@Database(
    entities = [
        AsteroidEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AsteroidRadarDataBase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

}
