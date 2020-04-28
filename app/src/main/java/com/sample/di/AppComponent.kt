package com.sample.di

import com.sample.core.data.di.CoreComponent
import com.sample.core.data.di.FeatureScope
import com.sample.search.SearchActivity
import com.sample.viewmodel.ViewModelModule
import dagger.Component

@Component(
    modules = [
        ViewModelModule::class
    ],
    dependencies = [
        CoreComponent::class
    ]
)
@FeatureScope
interface AppComponent {
    fun inject(activity: SearchActivity)

    @Component.Builder
    interface Builder {

        fun coreComponent(component: CoreComponent): Builder
        fun build(): AppComponent
    }
}