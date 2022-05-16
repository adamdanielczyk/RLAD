package com.rlad.infrastructure.rickandmorty.local

import com.rlad.infrastructure.common.local.AppPreferencesLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class RickAndMortyPreferencesLocalDataSource @Inject constructor(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
) {

    suspend fun getNextPageToLoad(): Int? = appPreferencesLocalDataSource.get(NEXT_PAGE_TO_LOAD).firstOrNull()?.toInt()

    suspend fun saveNextPageToLoad(page: Int) {
        appPreferencesLocalDataSource.save(
            key = NEXT_PAGE_TO_LOAD,
            value = page.toString()
        )
    }

    private companion object {
        const val NEXT_PAGE_TO_LOAD = "RICK_AND_MORTY_NEXT_PAGE_TO_LOAD"
    }
}