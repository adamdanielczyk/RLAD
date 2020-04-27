package com.sample.di

import com.sample.data.di.DataModule
import com.sample.details.DetailsActivity
import com.sample.search.SearchActivity
import com.sample.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    DataModule::class,
    ViewModelModule::class
])
interface SingletonComponent {
    fun inject(activity: SearchActivity)
    fun inject(activity: DetailsActivity)
}