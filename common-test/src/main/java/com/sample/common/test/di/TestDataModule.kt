package com.sample.common.test.di

import com.sample.common.test.remote.FakeRickAndMortyApi
import com.sample.core.data.local.CharacterDao
import com.sample.core.data.local.CharacterDatabase
import com.sample.core.data.remote.RickAndMortyApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDataModule {

    @Provides
    @Singleton
    fun characterDao(characterDatabase: CharacterDatabase): CharacterDao =
        characterDatabase.characterDao()

    @Provides
    fun rickAndMortyApi(fakeRickAndMortyApi: FakeRickAndMortyApi): RickAndMortyApi =
        fakeRickAndMortyApi
}