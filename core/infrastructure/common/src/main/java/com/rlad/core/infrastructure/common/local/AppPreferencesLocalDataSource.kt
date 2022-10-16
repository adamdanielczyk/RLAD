package com.rlad.core.infrastructure.common.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesLocalDataSource @Inject constructor(
    private val preferencesDao: AppPreferencesDao,
) {

    fun get(key: String): Flow<String?> = preferencesDao.getByKey(key).map { it?.value }

    suspend fun save(key: String, value: String) {
        preferencesDao.insert(AppPreferencesEntity(key, value))
    }
}