package com.sample

import android.app.Application
import com.sample.di.ContextModule
import com.sample.di.DaggerSingletonComponent
import com.sample.di.SingletonComponent

class SampleApplication : Application() {

    lateinit var component: SingletonComponent
        private set

    override fun onCreate() {
        super.onCreate()
        app = this
        component = DaggerSingletonComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {

        lateinit var app: SampleApplication
            private set
    }
}