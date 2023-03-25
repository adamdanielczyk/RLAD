package com.rlad.core.infrastructure.artic.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ArtworkDataDao {

    @Query(
        """
        SELECT * FROM artwork_data 
        ORDER BY id
        """
    )
    fun getAll(): PagingSource<Int, ArtworkDataEntity>

    @Query("SELECT * FROM artwork_data WHERE artic_id = :articId")
    fun getById(articId: Int): Flow<ArtworkDataEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artworks: List<ArtworkDataEntity>)

    @Query("DELETE FROM artwork_data")
    suspend fun clearTable()
}