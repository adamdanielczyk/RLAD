package com.rlad.core.infrastructure.artic.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ArtworkDao {

    @Query(
        """
        SELECT * FROM artic_artwork 
        ORDER BY id
        """
    )
    fun getAll(): PagingSource<Int, ArtworkEntity>

    @Query("SELECT * FROM artic_artwork WHERE artic_id = :articId")
    fun getById(articId: Int): Flow<ArtworkEntity?>

    @Upsert
    suspend fun insert(artworks: List<ArtworkEntity>)

    @Query("DELETE FROM artic_artwork")
    suspend fun clearTable()
}