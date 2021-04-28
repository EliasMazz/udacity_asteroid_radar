package com.udacity.asteroidradar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.AsteroidEntity

@Database(
    entities = [
        AsteroidEntity::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class AsteroidDataBase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

}
