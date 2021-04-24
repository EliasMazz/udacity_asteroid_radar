package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.common.dependencyinjection.CompositionRoot
import timber.log.Timber

class MyApplication : Application() {

    val applicationCompositionRoot by lazy { CompositionRoot(this) }

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
