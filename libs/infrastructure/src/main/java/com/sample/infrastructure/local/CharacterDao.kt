package com.sample.infrastructure.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {

    @Query(
        """
        SELECT * FROM character 
        WHERE name LIKE '%' || :name || '%' 
        ORDER BY id
        """
    )
    fun getBy(name: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getBy(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<CharacterEntity>)
}