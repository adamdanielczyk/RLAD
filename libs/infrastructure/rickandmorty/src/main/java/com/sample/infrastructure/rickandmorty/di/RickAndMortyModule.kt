package com.sample.infrastructure.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.sample.infrastructure.rickandmorty.local.CharacterDao
import com.sample.infrastructure.rickandmorty.local.RickAndMortyDatabase
import com.sample.infrastructure.rickandmorty.remote.RickAndMortyApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(RickAndMortyComponent::class)
@Module
internal object RickAndMortyModule {

    @Provides
    @RickAndMortyScope
    fun rickAndMortyDatabase(@ApplicationContext context: Context): RickAndMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickAndMortyDatabase::class.java,
            "rickandmorty_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @RickAndMortyScope
    fun characterDao(rickAndMortyDatabase: RickAndMortyDatabase): CharacterDao =
        rickAndMortyDatabase.characterDao()

    @Provides
    @RickAndMortyScope
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @RickAndMortyScope
    fun retrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @RickAndMortyScope
    fun rickAndMortyApi(retrofit: Retrofit): RickAndMortyApi =
        retrofit.create(RickAndMortyApi::class.java)
}