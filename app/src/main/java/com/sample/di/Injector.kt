package com.sample.di

import com.sample.SampleApplication

object Injector {

    fun get(): AppComponent = SampleApplication.app.component
}