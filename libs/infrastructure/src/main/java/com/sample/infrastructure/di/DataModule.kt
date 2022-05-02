package com.sample.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.sample.domain.repository.ItemsRepository
import com.sample.infrastructure.local.CharacterDao
import com.sample.infrastructure.local.CharacterDatabase
import com.sample.infrastructure.remote.RickAndMortyApi
import com.sample.infrastructure.repository.CharacterRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.Bindings::class])
internal object DataModule {

    @Provides
    @Singleton
    fun characterDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            "character_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun characterDao(characterDatabase: CharacterDatabase): CharacterDao =
        characterDatabase.characterDao()

    @Provides
    @Singleton
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun retrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun rickAndMortyApi(retrofit: Retrofit): RickAndMortyApi =
        retrofit.create(RickAndMortyApi::class.java)

    @InstallIn(SingletonComponent::class)
    @Module
    internal interface Bindings {

        @Binds
        fun bindItemsRepository(impl: CharacterRepository): ItemsRepository
    }
}