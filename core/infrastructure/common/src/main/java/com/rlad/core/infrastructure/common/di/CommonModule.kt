package com.rlad.core.infrastructure.common.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import com.rlad.core.domain.usecase.GetItemsUseCase
import com.rlad.core.infrastructure.common.local.AppPreferencesDao
import com.rlad.core.infrastructure.common.local.CommonDatabase
import com.rlad.core.infrastructure.common.paging.PagingDataRepository
import com.rlad.core.infrastructure.common.paging.PagingDataRepositoryImpl
import com.rlad.core.infrastructure.common.repository.AppSettingsRepositoryImpl
import com.rlad.core.infrastructure.common.usecase.GetAllDataSourcesUseCase
import com.rlad.core.infrastructure.common.usecase.GetAllDataSourcesUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetAvailableDataSourcesUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetCommonRepositoryUseCase
import com.rlad.core.infrastructure.common.usecase.GetCommonRepositoryUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetItemByIdUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetItemsUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetSelectedDataSourceUseCase
import com.rlad.core.infrastructure.common.usecase.GetSelectedDataSourceUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface CommonModule {

    companion object {

        @Provides
        @Singleton
        fun commonDatabase(application: Application): CommonDatabase {
            return Room.databaseBuilder(
                application,
                CommonDatabase::class.java,
                "common_database"
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun appPreferencesDao(commonDatabase: CommonDatabase): AppPreferencesDao = commonDatabase.appPreferencesDao()
    }

    @Binds
    fun AppSettingsRepositoryImpl.bindAppSettingsRepository(): AppSettingsRepository

    @Binds
    fun GetAvailableDataSourcesUseCaseImpl.bindGetAvailableDataSourcesUseCase(): GetAvailableDataSourcesUseCase

    @Binds
    fun GetItemByIdUseCaseImpl.bindGetItemByIdUseCase(): GetItemByIdUseCase

    @Binds
    fun GetItemsUseCaseImpl.bindGetItemsUseCase(): GetItemsUseCase

    @Binds
    fun GetCommonRepositoryUseCaseImpl.bindGetCommonRepositoryUseCase(): GetCommonRepositoryUseCase

    @Binds
    fun GetSelectedDataSourceUseCaseImpl.bindGetSelectedDataSourceUseCase(): GetSelectedDataSourceUseCase

    @Binds
    fun GetAllDataSourcesUseCaseImpl.bindGetAllDataSourcesUseCase(): GetAllDataSourcesUseCase

    @Binds
    fun PagingDataRepositoryImpl.bindPagingDataRepository(): PagingDataRepository
}