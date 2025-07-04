package com.rlad.core.infrastructure.artic.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.artic.local.ArticDatabase
import com.rlad.core.infrastructure.artic.local.ArticLocalDataSource
import com.rlad.core.infrastructure.artic.local.ArtworkDao
import com.rlad.core.infrastructure.artic.local.ArtworkEntity
import com.rlad.core.infrastructure.artic.mapper.ArticModelMapper
import com.rlad.core.infrastructure.artic.model.ArticDataSourceConfiguration
import com.rlad.core.infrastructure.artic.remote.ArticApi
import com.rlad.core.infrastructure.artic.remote.ArticRemoteDataSource
import com.rlad.core.infrastructure.artic.remote.ServerArtwork
import com.rlad.core.infrastructure.artic.remote.ServerArtworksRoot
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.model.DataSourceKey
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
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
internal interface ArticModule {

    @Qualifier
    annotation class ArticHttpClient

    companion object {

        @Provides
        @Singleton
        fun database(application: Application): ArticDatabase = Room.databaseBuilder(
            application,
            ArticDatabase::class.java,
            "artic_database",
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        @Provides
        fun dao(database: ArticDatabase): ArtworkDao = database.artworkDao()

        @Provides
        @Singleton
        @ArticHttpClient
        fun httpClient(): HttpClient = HttpClient(Android) {
            defaultRequest {
                url("https://api.artic.edu")
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
        fun api(@ArticHttpClient httpClient: HttpClient): ArticApi = ArticApi(httpClient)
    }

    @Binds
    fun bindCommonLocalDataSource(impl: ArticLocalDataSource): CommonLocalDataSource<ArtworkEntity>

    @Binds
    fun bindCommonRemoteDataSource(impl: ArticRemoteDataSource): CommonRemoteDataSource<ServerArtworksRoot, ServerArtwork>

    @Binds
    fun bindModelMapper(impl: ArticModelMapper): ModelMapper<ArtworkEntity, ServerArtwork>

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.ARTIC)
    fun bindCommonRepository(impl: CommonRepositoryImpl<ArtworkEntity, ServerArtwork, ServerArtworksRoot>): CommonRepository

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.ARTIC)
    fun bindDataSourceConfiguration(impl: ArticDataSourceConfiguration): DataSourceConfiguration
}
