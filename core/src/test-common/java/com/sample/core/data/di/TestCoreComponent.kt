package com.sample.core.data.di

import com.sample.core.data.local.CharacterDatabase
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.remote.CharacterRemoteDataSource
import com.sample.core.data.remote.FakeRickAndMortyApi
import com.sample.core.data.repository.CharacterRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        TestDataModule::class
    ]
)
@Singleton
interface TestCoreComponent {

    fun fakeRickAndMortyApi(): FakeRickAndMortyApi
    fun characterRepository(): CharacterRepository
    fun characterLocalDataSource(): CharacterLocalDataSource
    fun characterRemoteDataSource(): CharacterRemoteDataSource

    @Component.Builder
    interface Builder {

        @BindsInstance fun database(database: CharacterDatabase): Builder
        fun build(): TestCoreComponent
    }
}