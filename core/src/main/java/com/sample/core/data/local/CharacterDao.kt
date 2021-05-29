package com.sample.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query(
        """
        SELECT * FROM character 
        WHERE name LIKE '%' || :nameOrLocation || '%' 
        OR location_name LIKE '%' || :nameOrLocation || '%' 
        ORDER BY id
        """
    )
    fun getBy(nameOrLocation: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getBy(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<CharacterEntity>)
}