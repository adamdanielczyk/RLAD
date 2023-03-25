package com.rlad.core.infrastructure.artic.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.artic.local.ArticDatabase
import com.rlad.core.infrastructure.artic.local.ArticLocalDataSource
import com.rlad.core.infrastructure.artic.local.ArtworkDataDao
import com.rlad.core.infrastructure.artic.local.ArtworkDataEntity
import com.rlad.core.infrastructure.artic.mapper.ArticModelMapper
import com.rlad.core.infrastructure.artic.model.ArticDataSourceConfiguration
import com.rlad.core.infrastructure.artic.paging.ArticSearchPagingSourceFactory
import com.rlad.core.infrastructure.artic.remote.ArticApi
import com.rlad.core.infrastructure.artic.remote.ArticRemoteDataSource
import com.rlad.core.infrastructure.artic.remote.ServerArtworkData
import com.rlad.core.infrastructure.artic.remote.ServerArtworks
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.model.DataSourceKey
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSourceFactory
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
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
internal interface ArticModule {

    @Qualifier
    annotation class ArticRetrofit

    companion object {

        @Provides
        @Singleton
        fun articDatabase(application: Application): ArticDatabase = Room.databaseBuilder(
            application,
            ArticDatabase::class.java,
            "artic_database"
        ).fallbackToDestructiveMigration().build()

        @Provides
        fun artworkDataDao(articDatabase: ArticDatabase): ArtworkDataDao =
            articDatabase.artworkDataDao()

        @Provides
        @Singleton
        @ArticRetrofit
        fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.artic.edu")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun articApi(@ArticRetrofit retrofit: Retrofit): ArticApi =
            retrofit.create(ArticApi::class.java)
    }

    @Binds
    fun ArticLocalDataSource.bindCommonLocalDataSource(): CommonLocalDataSource<ArtworkDataEntity>

    @Binds
    fun ArticRemoteDataSource.bindCommonRemoteDataSource(): CommonRemoteDataSource<ServerArtworks, ServerArtworkData>

    @Binds
    fun ArticModelMapper.bindModelMapper(): ModelMapper<ArtworkDataEntity, ServerArtworkData>

    @Binds
    fun ArticSearchPagingSourceFactory.bindCommonSearchPagingSourceFactory(): CommonSearchPagingSourceFactory<ServerArtworkData>

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.ARTIC)
    fun CommonRepositoryImpl<ArtworkDataEntity, ServerArtworkData, ServerArtworks>.bindCommonRepository(): CommonRepository

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.ARTIC)
    fun ArticDataSourceConfiguration.bindDataSourceConfiguration(): DataSourceConfiguration
}