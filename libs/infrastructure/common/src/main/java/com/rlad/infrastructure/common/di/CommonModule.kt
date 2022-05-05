package com.rlad.infrastructure.common.di

import android.content.Context
import android.content.SharedPreferences
import com.rlad.domain.repository.AppSettingsRepository
import com.rlad.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.domain.usecase.GetItemByIdUseCase
import com.rlad.domain.usecase.GetItemsUseCase
import com.rlad.infrastructure.common.repository.AppSettingsRepositoryImpl
import com.rlad.infrastructure.common.usecase.GetAvailableDataSourcesUseCaseImpl
import com.rlad.infrastructure.common.usecase.GetItemByIdUseCaseImpl
import com.rlad.infrastructure.common.usecase.GetItemsUseCaseImpl
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