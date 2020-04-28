package com.sample.features.search.di

import com.sample.core.data.di.CoreComponent
import com.sample.core.data.di.FeatureScope
import com.sample.features.search.SearchActivity
import dagger.Component

@Component(
    modules = [
        SearchModule::class
    ],
    dependencies = [
        CoreComponent::class
    ]
)
@FeatureScope
interface SearchComponent {

    fun inject(searchActivity: SearchActivity)

    @Component.Builder
    interface Builder {

        fun searchModule(searchModule: SearchModule): Builder
        fun coreComponent(component: CoreComponent): Builder
        fun build(): SearchComponent
    }
}