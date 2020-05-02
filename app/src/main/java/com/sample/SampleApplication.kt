package com.sample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sample.core.data.di.CoreComponent
import com.sample.core.data.di.CoreComponentProvider
import com.sample.core.data.di.DaggerCoreComponent
import java.util.Random

@Suppress("unused")
class SampleApplication : Application(), CoreComponentProvider {

    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupRandomNightMode()
    }

    private fun setupDagger() {
        coreComponent = DaggerCoreComponent.builder()
            .appContext(this)
            .build()
    }

    private fun setupRandomNightMode() {
        val nightMode = when (Random().nextBoolean()) {
            true -> AppCompatDelegate.MODE_NIGHT_YES
            false -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent
}