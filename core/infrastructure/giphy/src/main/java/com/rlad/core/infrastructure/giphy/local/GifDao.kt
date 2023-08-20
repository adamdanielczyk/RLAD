package com.rlad.core.infrastructure.giphy.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GifDao {

    @Query(
        """
        SELECT * FROM giphy_gif 
        ORDER BY id
        """
    )
    fun getAll(): PagingSource<Int, GifEntity>

    @Query("SELECT * FROM giphy_gif WHERE giphy_id = :giphyId")
    fun getById(giphyId: String): Flow<GifEntity?>

    @Upsert
    suspend fun insert(gifs: List<GifEntity>)

    @Query("DELETE FROM giphy_gif")
    suspend fun clearTable()
}