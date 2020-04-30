package com.sample.core.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character WHERE name LIKE '%' || :name || '%' ORDER BY id")
    fun getByName(name: String): DataSource.Factory<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getById(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<CharacterEntity>)
}