package com.udacity.asteroidradar.features.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.asteroidradar.MyApplication
import com.udacity.asteroidradar.R

class MainActivity : AppCompatActivity() {

    val compositionRoot by lazy {
        (application as MyApplication).applicationCompositionRoot
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
