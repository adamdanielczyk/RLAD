package com.rlad.infrastructure.giphy.di

import com.rlad.infrastructure.giphy.repository.GiphyRepository
import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Scope

@Scope
@Retention
internal annotation class GiphyScope

@GiphyScope
@DefineComponent(parent = SingletonComponent::class)
internal interface GiphyComponent {

    @DefineComponent.Builder
    interface Builder {
        fun build(): GiphyComponent
    }
}

@InstallIn(GiphyComponent::class)
@EntryPoint
internal interface GiphyEntryPoint {
    fun giphyRepository(): GiphyRepository
}
