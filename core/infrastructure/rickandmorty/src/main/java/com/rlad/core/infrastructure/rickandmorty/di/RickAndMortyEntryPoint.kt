package com.rlad.core.infrastructure.rickandmorty.di

import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.rickandmorty.model.RickAndMortyDataSourceConfiguration
import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Scope

@Scope
@Retention
internal annotation class RickAndMortyScope

@RickAndMortyScope
@DefineComponent(parent = SingletonComponent::class)
internal interface RickAndMortyComponent {

    @DefineComponent.Builder
    interface Builder {
        fun build(): RickAndMortyComponent
    }
}

@InstallIn(RickAndMortyComponent::class)
@EntryPoint
internal interface RickAndMortyEntryPoint {
    fun dataSourceConfiguration(): RickAndMortyDataSourceConfiguration
    fun commonRepository(): CommonRepository
}