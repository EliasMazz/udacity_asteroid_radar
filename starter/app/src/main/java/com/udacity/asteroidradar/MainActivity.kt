package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.asteroidradar.common.dependencyinjection.CompositionRoot

class MainActivity : AppCompatActivity() {

    val compositionRoot by lazy {
        (application as MyApplication).applicationCompositionRoot
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
