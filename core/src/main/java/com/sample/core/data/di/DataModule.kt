package com.sample.core.data.di

import android.content.Context
import androidx.room.Room
import com.sample.core.data.local.CharacterDao
import com.sample.core.data.local.CharacterDatabase
import com.sample.core.data.remote.RickAndMortyApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun characterDatabase(context: Context): CharacterDatabase {
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
    fun mosh(): Moshi {
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
}