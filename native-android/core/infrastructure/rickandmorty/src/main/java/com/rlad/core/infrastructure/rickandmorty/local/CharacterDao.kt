package com.rlad.core.infrastructure.rickandmorty.local

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
        SELECT * FROM rickandmorty_character 
        ORDER BY id
        """,
    )
    fun getAll(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM rickandmorty_character WHERE id = :id")
    fun getById(id: Int): Flow<CharacterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<CharacterEntity>)

    @Query("DELETE FROM rickandmorty_character")
    suspend fun clearTable()
}
