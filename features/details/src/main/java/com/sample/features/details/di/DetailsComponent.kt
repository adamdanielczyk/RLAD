package com.sample.features.details.di

import com.sample.core.data.di.CoreComponent
import com.sample.core.data.di.FeatureScope
import com.sample.features.details.DetailsActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DetailsModule::class
    ],
    dependencies = [
        CoreComponent::class
    ]
)
@FeatureScope
interface DetailsComponent {

    fun inject(detailsActivity: DetailsActivity)

    @Component.Builder
    interface Builder {

        fun detailsModule(detailsModule: DetailsModule): Builder
        @BindsInstance fun characterId(characterId: Int): Builder
        fun coreComponent(component: CoreComponent): Builder
        fun build(): DetailsComponent
    }
}