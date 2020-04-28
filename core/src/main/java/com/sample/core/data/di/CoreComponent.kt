package com.sample.core.data.di

import android.content.Context
import com.sample.core.data.repository.CharacterRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        DataModule::class
    ]
)
@Singleton
interface CoreComponent {

    fun characterRepository(): CharacterRepository

    @Component.Builder
    interface Builder {

        @BindsInstance fun appContext(context: Context): Builder
        fun build(): CoreComponent
    }
}