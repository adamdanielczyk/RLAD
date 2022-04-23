package com.sample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupRandomNightMode()
    }

    private fun setupRandomNightMode() {
        val nightMode = if (Random().nextBoolean()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}