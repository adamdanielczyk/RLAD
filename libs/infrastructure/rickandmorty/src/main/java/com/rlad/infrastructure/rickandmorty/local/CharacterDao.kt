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
        WHERE name LIKE '%' || :name || '%' 
        ORDER BY id
        """
    )
    fun getByName(name: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getById(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<CharacterEntity>)
}