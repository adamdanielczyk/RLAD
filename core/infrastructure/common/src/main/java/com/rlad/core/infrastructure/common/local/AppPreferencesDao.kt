package com.rlad.core.infrastructure.common.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AppPreferencesDao {

    @Query("SELECT * FROM app_preferences WHERE preference_key = :key")
    fun getByKey(key: String): Flow<AppPreferencesEntity?>

    @Upsert
    suspend fun insert(appPreferencesEntity: AppPreferencesEntity)
}