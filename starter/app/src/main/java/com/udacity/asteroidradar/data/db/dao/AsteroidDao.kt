package com.udacity.asteroidradar.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.db.model.AsteroidEntity

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: AsteroidEntity)

    @Query("SELECT * FROM asteroidentity")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

}
