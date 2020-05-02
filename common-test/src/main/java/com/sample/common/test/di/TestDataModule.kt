package com.sample.common.test.di

import com.sample.common.test.remote.FakeRickAndMortyApi
import com.sample.core.data.local.CharacterDao
import com.sample.core.data.local.CharacterDatabase
import com.sample.core.data.remote.RickAndMortyApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
class TestDataModule {

    @Provides
    @Singleton
    fun ioDispatcher(): CoroutineDispatcher = TestCoroutineDispatcher()

    @Provides
    @Singleton
    fun fakeDiskExecutor(): Executor = Executor { runnable -> runnable.run() }

    @Provides
    @Singleton
    fun characterDao(characterDatabase: CharacterDatabase): CharacterDao =
        characterDatabase.characterDao()

    @Provides
    fun rickAndMortyApi(fakeRickAndMortyApi: FakeRickAndMortyApi): RickAndMortyApi =
        fakeRickAndMortyApi
}