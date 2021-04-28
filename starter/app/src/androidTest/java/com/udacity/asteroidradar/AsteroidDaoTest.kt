package com.udacity.asteroidradar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.udacity.asteroidradar.data.db.AsteroidDataBase
import com.udacity.asteroidradar.data.db.dao.AsteroidDao
import com.udacity.asteroidradar.data.db.model.AsteroidEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.lang.Exception
import java.time.LocalDate
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class AsteroidDaoTest {
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()
    private lateinit var database: AsteroidDataBase
    private lateinit var asteroidDao: AsteroidDao


    private val asteroid0 = AsteroidEntity(
        id = 0,
        codename = "2007 WQ3",
        closeApproachDate = LocalDate.now(),
        absoluteMagnitude = 21.2,
        estimatedDiameter = 0.3420109247,
        relativeVelocity = 9.8433946985,
        distanceFromEarth = 0.461789067,
        isPotentiallyHazardous = false
    )

    private val asteroid1 = AsteroidEntity(
        id = 1,
        codename = "3001",
        closeApproachDate = LocalDate.now().plusDays(5),
        absoluteMagnitude = 245.2,
        estimatedDiameter = 0.111109247,
        relativeVelocity = 11.322985,
        distanceFromEarth = 0.411789067,
        isPotentiallyHazardous = true
    )


    private val asteroid2 = AsteroidEntity(
        id = 2,
        codename = "55 HEY ",
        closeApproachDate = LocalDate.now().plusDays(3),
        absoluteMagnitude = 435.2,
        estimatedDiameter = 0.1921109247,
        relativeVelocity = 15.322985,
        distanceFromEarth = 0.052,
        isPotentiallyHazardous = false
    )

    private val listOfAsteroidsEntity = listOf(
        asteroid0, asteroid1, asteroid2
    )


    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        try {
            database = Room.inMemoryDatabaseBuilder(context, AsteroidDataBase::class.java)
                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                .build()
        } catch (e: Exception) {
            Logger.i(this.javaClass.simpleName, "" + e.message)
        }
        asteroidDao = database.asteroidDao()
    }

    @Test
    fun testInsertAndRetreiveAsteroids() {
        runBlocking {

            insertAsteroidsToDatabase()
            val listOfAsteroidsFromDB = asteroidDao.getAllAsteroids()

            assertEquals(listOfAsteroidsEntity, listOfAsteroidsFromDB)
        }
    }

    @Test
    fun testRetrieveAsteroidByDate() {
        runBlocking {

            insertAsteroidsToDatabase()
            val asteroidFromDb = asteroidDao.getAsteroidsByDate(LocalDate.now().plusDays(3))

            assertEquals(true, asteroidFromDb.contains(asteroid2))
        }
    }

    @Test
    fun testRetrieveAsteroidByDateRange() {
        runBlocking {

            insertAsteroidsToDatabase()
            val asteroidFromDb = asteroidDao.getAsteroidsByDateRange(
                LocalDate.now(),
                LocalDate.now().plusDays(3)
            )

            assertEquals(true, asteroidFromDb.contains(asteroid0))
            assertEquals(false, asteroidFromDb.contains(asteroid1))
            assertEquals(true, asteroidFromDb.contains(asteroid2))

        }
    }

    private fun insertAsteroidsToDatabase() {
        runBlocking {
            asteroidDao.insertAll(*listOfAsteroidsEntity.toTypedArray())
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

}
