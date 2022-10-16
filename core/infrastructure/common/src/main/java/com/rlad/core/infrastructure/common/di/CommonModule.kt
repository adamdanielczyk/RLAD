package com.rlad.core.infrastructure.common.di

import android.content.Context
import androidx.room.Room
import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import com.rlad.core.domain.usecase.GetItemsUseCase
import com.rlad.core.infrastructure.common.local.AppPreferencesDao
import com.rlad.core.infrastructure.common.local.CommonDatabase
import com.rlad.core.infrastructure.common.repository.AppSettingsRepositoryImpl
import com.rlad.core.infrastructure.common.usecase.GetAllItemsRepositoriesUseCase
import com.rlad.core.infrastructure.common.usecase.GetAllItemsRepositoriesUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetAvailableDataSourcesUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetItemByIdUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetItemsUseCaseImpl
import com.rlad.core.infrastructure.common.usecase.GetSelectedItemsRepositoryUseCase
import com.rlad.core.infrastructure.common.usecase.GetSelectedItemsRepositoryUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [CommonModule.Bindings::class])
internal class CommonModule {

    @Provides
    @Singleton
    fun commonDatabase(@ApplicationContext context: Context): CommonDatabase {
        return Room.databaseBuilder(
            context,
            CommonDatabase::class.java,
            "common_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun appPreferencesDao(commonDatabase: CommonDatabase): AppPreferencesDao = commonDatabase.appPreferencesDao()

    @InstallIn(SingletonComponent::class)
    @Module
    interface Bindings {

        @Binds
        fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

        @Binds
        fun bindGetAvailableDataSourcesUseCase(impl: GetAvailableDataSourcesUseCaseImpl): GetAvailableDataSourcesUseCase

        @Binds
        fun bindGetItemByIdUseCase(impl: GetItemByIdUseCaseImpl): GetItemByIdUseCase

        @Binds
        fun bindGetItemsUseCase(impl: GetItemsUseCaseImpl): GetItemsUseCase

        @Binds
        fun bindGetSelectedItemsRepositoryUseCase(impl: GetSelectedItemsRepositoryUseCaseImpl): GetSelectedItemsRepositoryUseCase

        @Binds
        fun bindGetAllItemsRepositoriesUseCase(impl: GetAllItemsRepositoriesUseCaseImpl): GetAllItemsRepositoriesUseCase
    }
}