package com.rlad.core.infrastructure.giphy.di

import android.content.Context
import androidx.room.Room
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
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
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(GiphyComponent::class)
@Module(includes = [GiphyModule.Bindings::class])
internal object GiphyModule {

    @Provides
    @GiphyScope
    fun giphyDatabase(@ApplicationContext context: Context): GiphyDatabase {
        return Room.databaseBuilder(
            context,
            GiphyDatabase::class.java,
            "giphy_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @GiphyScope
    fun gifDataDao(giphyDatabase: GiphyDatabase): GifDataDao =
        giphyDatabase.gifDataDao()

    @Provides
    @GiphyScope
    fun giphyApi(retrofit: Retrofit): GiphyApi =
        retrofit.create(GiphyApi::class.java)

    @Provides
    @GiphyScope
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.giphy.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @InstallIn(GiphyComponent::class)
    @Module
    interface Bindings {

        @Binds
        fun bindCommonLocalDataSource(impl: GiphyLocalDataSource): CommonLocalDataSource<GifDataEntity>

        @Binds
        fun bindCommonRemoteDataSource(impl: GiphyRemoteDataSource): CommonRemoteDataSource<ServerGifs, ServerGifData>

        @Binds
        fun bindModelMapper(impl: GiphyModelMapper): ModelMapper<GifDataEntity, ServerGifData>

        @Binds
        fun bindCommonSearchPagingSourceFactory(impl: GiphySearchPagingSourceFactory): CommonSearchPagingSourceFactory<ServerGifData>

        @Binds
        fun bindCommonRepository(impl: CommonRepositoryImpl<GifDataEntity, ServerGifData, ServerGifs>): CommonRepository

        @Binds
        fun bindDataSourceConfiguration(impl: GiphyDataSourceConfiguration): DataSourceConfiguration
    }
}