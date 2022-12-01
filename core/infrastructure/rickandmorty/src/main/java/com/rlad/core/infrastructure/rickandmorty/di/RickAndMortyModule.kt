package com.rlad.core.infrastructure.rickandmorty.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.model.DataSourceKey
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
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface RickAndMortyModule {

    @Qualifier
    annotation class RickAndMortyRetrofit

    companion object {

        @Provides
        @Singleton
        fun rickAndMortyDatabase(application: Application): RickAndMortyDatabase = Room.databaseBuilder(
            application,
            RickAndMortyDatabase::class.java,
            "rickandmorty_database"
        ).fallbackToDestructiveMigration().build()

        @Provides
        fun characterDao(rickAndMortyDatabase: RickAndMortyDatabase): CharacterDao =
            rickAndMortyDatabase.characterDao()

        @Provides
        @Singleton
        @RickAndMortyRetrofit
        fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun rickAndMortyApi(@RickAndMortyRetrofit retrofit: Retrofit): RickAndMortyApi =
            retrofit.create(RickAndMortyApi::class.java)
    }

    @Binds
    fun RickAndMortyLocalDataSource.bindCommonLocalDataSource(): CommonLocalDataSource<CharacterEntity>

    @Binds
    fun RickAndMortyRemoteDataSource.bindCommonRemoteDataSource(): CommonRemoteDataSource<ServerGetCharacters, ServerCharacter>

    @Binds
    fun RickAndMortyModelMapper.bindModelMapper(): ModelMapper<CharacterEntity, ServerCharacter>

    @Binds
    fun RickAndMortySearchPagingSourceFactory.bindCommonSearchPagingSourceFactory(): CommonSearchPagingSourceFactory<ServerCharacter>

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.RICKANDMORTY)
    fun CommonRepositoryImpl<CharacterEntity, ServerCharacter, ServerGetCharacters>.bindCommonRepository(): CommonRepository

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.RICKANDMORTY)
    fun RickAndMortyDataSourceConfiguration.bindDataSourceConfiguration(): DataSourceConfiguration
}