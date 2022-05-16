package com.rlad.infrastructure.common.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppPreferencesDao {

    @Query("SELECT * FROM app_preferences WHERE preference_key = :key")
    fun getByKey(key: String): Flow<AppPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appPreferencesEntity: AppPreferencesEntity)
}