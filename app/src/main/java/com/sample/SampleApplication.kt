package com.sample

import android.app.Application
import com.sample.core.data.di.DaggerCoreComponent
import com.sample.di.AppComponent
import com.sample.di.DaggerAppComponent

class SampleApplication : Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        app = this

        setupDagger()
    }

    private fun setupDagger() {
        val coreComponent = DaggerCoreComponent.builder()
            .appContext(this)
            .build()

        component = DaggerAppComponent.builder()
            .coreComponent(coreComponent)
            .build()
    }

    companion object {

        lateinit var app: SampleApplication
            private set
    }
}