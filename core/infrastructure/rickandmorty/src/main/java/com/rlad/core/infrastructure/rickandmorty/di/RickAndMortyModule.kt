package com.rlad.core.infrastructure.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.rlad.core.infrastructure.rickandmorty.local.CharacterDao
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyDatabase
import com.rlad.core.infrastructure.rickandmorty.remote.RickAndMortyApi
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
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @RickAndMortyScope
    fun rickAndMortyApi(retrofit: Retrofit): RickAndMortyApi =
        retrofit.create(RickAndMortyApi::class.java)
}