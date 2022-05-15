package com.rlad.infrastructure.giphy.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface GiphyPreferencesDao {

    @Query("SELECT * FROM giphy_preferences WHERE preference_key = :key")
    suspend fun getByKey(key: String): GiphyPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(giphyPreferencesEntity: GiphyPreferencesEntity)
}