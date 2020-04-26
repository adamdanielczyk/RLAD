package com.sample.di

import com.sample.MainActivity
import com.sample.data.di.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    DataModule::class
])
interface SingletonComponent {
    fun inject(mainActivity: MainActivity)
}