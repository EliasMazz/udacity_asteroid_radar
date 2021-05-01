package com.udacity.asteroidradar.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.MyApplication
import retrofit2.HttpException

class RefreshDataWork(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val refreshAsteroidListUseCase = (applicationContext as MyApplication).applicationCompositionRoot.refreshAsteroidListUseCase
            refreshAsteroidListUseCase.invoke()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}

