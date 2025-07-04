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
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface RickAndMortyModule {

    @Qualifier
    annotation class RickAndMortyHttpClient

    companion object {

        @Provides
        @Singleton
        fun database(application: Application): RickAndMortyDatabase = Room.databaseBuilder(
            application,
            RickAndMortyDatabase::class.java,
            "rickandmorty_database",
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        @Provides
        fun dao(database: RickAndMortyDatabase): CharacterDao = database.characterDao()

        @Provides
        @Singleton
        @RickAndMortyHttpClient
        fun httpClient(): HttpClient = HttpClient(Android) {
            defaultRequest {
                url("https://rickandmortyapi.com")
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    },
                )
            }
        }

        @Provides
        @Singleton
        fun api(@RickAndMortyHttpClient httpClient: HttpClient): RickAndMortyApi = RickAndMortyApi(httpClient)
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
