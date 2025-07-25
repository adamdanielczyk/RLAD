package com.rlad.core.infrastructure.giphy.di

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
import com.rlad.core.infrastructure.giphy.local.GifDao
import com.rlad.core.infrastructure.giphy.local.GifEntity
import com.rlad.core.infrastructure.giphy.local.GiphyDatabase
import com.rlad.core.infrastructure.giphy.local.GiphyLocalDataSource
import com.rlad.core.infrastructure.giphy.mapper.GiphyModelMapper
import com.rlad.core.infrastructure.giphy.model.GiphyDataSourceConfiguration
import com.rlad.core.infrastructure.giphy.remote.GiphyApi
import com.rlad.core.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.core.infrastructure.giphy.remote.ServerGif
import com.rlad.core.infrastructure.giphy.remote.ServerGifsRoot
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
internal interface GiphyModule {

    @Qualifier
    annotation class GiphyHttpClient

    companion object {

        @Provides
        @Singleton
        fun database(application: Application): GiphyDatabase = Room.databaseBuilder(
            application,
            GiphyDatabase::class.java,
            "giphy_database",
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        @Provides
        fun dao(database: GiphyDatabase): GifDao = database.gifDao()

        @Provides
        @Singleton
        @GiphyHttpClient
        fun httpClient(): HttpClient = HttpClient(Android) {
            defaultRequest {
                url("https://api.giphy.com")
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }

        @Provides
        @Singleton
        fun api(@GiphyHttpClient httpClient: HttpClient): GiphyApi = GiphyApi(httpClient)
    }

    @Binds
    fun bindCommonLocalDataSource(impl: GiphyLocalDataSource): CommonLocalDataSource<GifEntity>

    @Binds
    fun bindCommonRemoteDataSource(impl: GiphyRemoteDataSource): CommonRemoteDataSource<ServerGifsRoot, ServerGif>

    @Binds
    fun bindModelMapper(impl: GiphyModelMapper): ModelMapper<GifEntity, ServerGif>

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun bindCommonRepository(impl: CommonRepositoryImpl<GifEntity, ServerGif, ServerGifsRoot>): CommonRepository

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun bindDataSourceConfiguration(impl: GiphyDataSourceConfiguration): DataSourceConfiguration
}
