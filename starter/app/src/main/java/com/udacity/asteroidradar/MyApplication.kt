package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.common.dependencyinjection.CompositionRoot

class MyApplication : Application() {

    val applicationCompositionRoot = CompositionRoot()

    override fun onCreate() {
        super.onCreate()
    }
}
