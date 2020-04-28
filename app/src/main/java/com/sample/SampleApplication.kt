package com.sample

import android.app.Application
import com.sample.core.data.di.CoreComponent
import com.sample.core.data.di.CoreComponentProvider
import com.sample.core.data.di.DaggerCoreComponent
import com.sample.di.AppComponent
import com.sample.di.DaggerAppComponent

class SampleApplication : Application(), CoreComponentProvider {

    lateinit var coreComponent: CoreComponent
        private set

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        app = this
        setupDagger()
    }

    private fun setupDagger() {
        coreComponent = DaggerCoreComponent.builder()
            .appContext(this)
            .build()

        component = DaggerAppComponent.builder()
            .coreComponent(coreComponent)
            .build()
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent

    companion object {
        lateinit var app: SampleApplication
            private set
    }
}