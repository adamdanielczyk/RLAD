package com.rlad.infrastructure.giphy.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GifDataDao {

    @Query(
        """
        SELECT * FROM gif_data 
        ORDER BY id
        """
    )
    fun getAll(): PagingSource<Int, GifDataEntity>

    @Query("SELECT * FROM gif_data WHERE giphy_id = :giphyId")
    fun getById(giphyId: String): Flow<GifDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gifData: List<GifDataEntity>)

    @Query("DELETE FROM gif_data")
    suspend fun clearTable()
}