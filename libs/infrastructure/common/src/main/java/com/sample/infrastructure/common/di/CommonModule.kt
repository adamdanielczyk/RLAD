package com.sample.infrastructure.common.di

import android.content.Context
import android.content.SharedPreferences
import com.sample.domain.repository.AppSettingsRepository
import com.sample.domain.usecase.GetAvailableDataSourcesUseCase
import com.sample.domain.usecase.GetItemByIdUseCase
import com.sample.domain.usecase.GetItemsUseCase
import com.sample.infrastructure.common.repository.AppSettingsRepositoryImpl
import com.sample.infrastructure.common.usecase.GetAvailableDataSourcesUseCaseImpl
import com.sample.infrastructure.common.usecase.GetItemByIdUseCaseImpl
import com.sample.infrastructure.common.usecase.GetItemsUseCaseImpl
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
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

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
    }
}