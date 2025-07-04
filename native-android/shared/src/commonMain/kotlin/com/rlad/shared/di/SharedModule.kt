package com.rlad.shared.di

import com.rlad.shared.data.common.repository.AppSettingsRepositoryImpl
import com.rlad.shared.domain.repository.AppSettingsRepository
import com.rlad.shared.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.shared.domain.usecase.GetItemByIdUseCase
import com.rlad.shared.domain.usecase.GetItemsUseCase
import com.rlad.shared.domain.usecase.impl.GetAvailableDataSourcesUseCaseImpl
import com.rlad.shared.domain.usecase.impl.GetItemByIdUseCaseImpl
import com.rlad.shared.domain.usecase.impl.GetItemsUseCaseImpl
import org.koin.dsl.module

val sharedModule = module {
    // Repository layer (simplified - database integration can be added later)
    single<AppSettingsRepository> { AppSettingsRepositoryImpl() }
    
    // Use cases
    single<GetAvailableDataSourcesUseCase> { GetAvailableDataSourcesUseCaseImpl() }
    single<GetItemsUseCase> { GetItemsUseCaseImpl(get()) }
    single<GetItemByIdUseCase> { GetItemByIdUseCaseImpl() }
}