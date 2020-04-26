package com.sample.di

import com.sample.SampleApplication

object Injector {

    fun get(): SingletonComponent = SampleApplication.app.component
}