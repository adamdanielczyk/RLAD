package com.rlad.core.infrastructure.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSourceFactory
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
import com.rlad.core.infrastructure.rickandmorty.local.CharacterDao
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyDatabase
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.rlad.core.infrastructure.rickandmorty.mapper.RickAndMortyModelMapper
import com.rlad.core.infrastructure.rickandmorty.model.RickAndMortyDataSourceConfiguration
import com.rlad.core.infrastructure.rickandmorty.paging.RickAndMortySearchPagingSourceFactory
import com.rlad.core.infrastructure.rickandmorty.remote.RickAndMortyApi
import com.rlad.core.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter
import com.rlad.core.infrastructure.rickandmorty.remote.ServerGetCharacters
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(RickAndMortyComponent::class)
@Module(includes = [RickAndMortyModule.Bindings::class])
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

    @InstallIn(RickAndMortyComponent::class)
    @Module
    interface Bindings {

        @Binds
        fun bindCommonLocalDataSource(impl: RickAndMortyLocalDataSource): CommonLocalDataSource<CharacterEntity>

        @Binds
        fun bindCommonRemoteDataSource(impl: RickAndMortyRemoteDataSource): CommonRemoteDataSource<ServerGetCharacters, ServerCharacter>

        @Binds
        fun bindModelMapper(impl: RickAndMortyModelMapper): ModelMapper<CharacterEntity, ServerCharacter>

        @Binds
        fun bindCommonSearchPagingSourceFactory(impl: RickAndMortySearchPagingSourceFactory): CommonSearchPagingSourceFactory<ServerCharacter>

        @Binds
        fun bindCommonRepository(impl: CommonRepositoryImpl<CharacterEntity, ServerCharacter, ServerGetCharacters>): CommonRepository

        @Binds
        fun bindDataSourceConfiguration(impl: RickAndMortyDataSourceConfiguration): DataSourceConfiguration
    }
}