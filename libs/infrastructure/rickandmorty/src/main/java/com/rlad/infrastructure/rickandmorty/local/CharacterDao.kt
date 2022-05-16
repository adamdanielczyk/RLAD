package com.rlad.infrastructure.rickandmorty.local

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
        ORDER BY id
        """
    )
    fun getAll(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getById(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<CharacterEntity>)

    @Query("DELETE FROM character")
    suspend fun clearTable()
}