package com.sample

import android.app.Application
import com.sample.core.data.di.CoreComponent
import com.sample.core.data.di.CoreComponentProvider
import com.sample.core.data.di.DaggerCoreComponent

@Suppress("unused")
class SampleApplication : Application(), CoreComponentProvider {

    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        setupDagger()
    }

    private fun setupDagger() {
        coreComponent = DaggerCoreComponent.builder()
            .appContext(this)
            .build()
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent
}