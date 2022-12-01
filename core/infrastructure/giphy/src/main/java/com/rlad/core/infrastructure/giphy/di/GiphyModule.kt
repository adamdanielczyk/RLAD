package com.rlad.core.infrastructure.giphy.di

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
import com.rlad.core.infrastructure.giphy.local.GifDataDao
import com.rlad.core.infrastructure.giphy.local.GifDataEntity
import com.rlad.core.infrastructure.giphy.local.GiphyDatabase
import com.rlad.core.infrastructure.giphy.local.GiphyLocalDataSource
import com.rlad.core.infrastructure.giphy.mapper.GiphyModelMapper
import com.rlad.core.infrastructure.giphy.model.GiphyDataSourceConfiguration
import com.rlad.core.infrastructure.giphy.paging.GiphySearchPagingSourceFactory
import com.rlad.core.infrastructure.giphy.remote.GiphyApi
import com.rlad.core.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.core.infrastructure.giphy.remote.ServerGifData
import com.rlad.core.infrastructure.giphy.remote.ServerGifs
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
internal interface GiphyModule {

    @Qualifier
    annotation class GiphyRetrofit

    companion object {

        @Provides
        @Singleton
        fun giphyDatabase(application: Application): GiphyDatabase = Room.databaseBuilder(
            application,
            GiphyDatabase::class.java,
            "giphy_database"
        ).fallbackToDestructiveMigration().build()

        @Provides
        fun gifDataDao(giphyDatabase: GiphyDatabase): GifDataDao =
            giphyDatabase.gifDataDao()

        @Provides
        @Singleton
        @GiphyRetrofit
        fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun giphyApi(@GiphyRetrofit retrofit: Retrofit): GiphyApi =
            retrofit.create(GiphyApi::class.java)
    }

    @Binds
    fun GiphyLocalDataSource.bindCommonLocalDataSource(): CommonLocalDataSource<GifDataEntity>

    @Binds
    fun GiphyRemoteDataSource.bindCommonRemoteDataSource(): CommonRemoteDataSource<ServerGifs, ServerGifData>

    @Binds
    fun GiphyModelMapper.bindModelMapper(): ModelMapper<GifDataEntity, ServerGifData>

    @Binds
    fun GiphySearchPagingSourceFactory.bindCommonSearchPagingSourceFactory(): CommonSearchPagingSourceFactory<ServerGifData>

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun CommonRepositoryImpl<GifDataEntity, ServerGifData, ServerGifs>.bindCommonRepository(): CommonRepository

    @Binds
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun GiphyDataSourceConfiguration.bindDataSourceConfiguration(): DataSourceConfiguration
}