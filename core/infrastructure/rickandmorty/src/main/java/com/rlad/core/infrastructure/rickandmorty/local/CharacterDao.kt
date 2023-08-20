package com.rlad.core.infrastructure.rickandmorty.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {

    @Query(
        """
        SELECT * FROM rickandmorty_character 
        ORDER BY id
        """
    )
    fun getAll(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM rickandmorty_character WHERE id = :id")
    fun getById(id: Int): Flow<CharacterEntity?>

    @Upsert
    suspend fun insert(characters: List<CharacterEntity>)

    @Query("DELETE FROM rickandmorty_character")
    suspend fun clearTable()
}