package com.rlad.core.infrastructure.rickandmorty.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.model.DataSourceKey
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
import com.rlad.core.infrastructure.rickandmorty.local.CharacterDao
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyDatabase
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.rlad.core.infrastructure.rickandmorty.mapper.RickAndMortyModelMapper
import com.rlad.core.infrastructure.rickandmorty.model.RickAndMortyDataSourceConfiguration
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
        fun database(application: Application): RickAndMortyDatabase = Room.databaseBuilder(
            application,
            RickAndMortyDatabase::class.java,
            "rickandmorty_database"
        ).fallbackToDestructiveMigration().build()

        @Provides
        fun dao(database: RickAndMortyDatabase): CharacterDao = database.characterDao()

        @Provides
        @Singleton
        @RickAndMortyRetrofit
        fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun api(@RickAndMortyRetrofit retrofit: Retrofit): RickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)
    }

    @Binds
    fun bindCommonLocalDataSource(impl: RickAndMortyLocalDataSource): CommonLocalDataSource<CharacterEntity>

    @Binds
    fun bindCommonRemoteDataSource(impl: RickAndMortyRemoteDataSource): CommonRemoteDataSource<ServerGetCharacters, ServerCharacter>

    @Binds
    fun bindModelMapper(impl: RickAndMortyModelMapper): ModelMapper<CharacterEntity, ServerCharacter>

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.RICKANDMORTY)
    fun bindCommonRepository(impl: CommonRepositoryImpl<CharacterEntity, ServerCharacter, ServerGetCharacters>): CommonRepository

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.RICKANDMORTY)
    fun bindDataSourceConfiguration(impl: RickAndMortyDataSourceConfiguration): DataSourceConfiguration
}