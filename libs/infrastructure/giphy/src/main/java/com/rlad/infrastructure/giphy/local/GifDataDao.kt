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
        WHERE title LIKE '%' || :title || '%'
        AND origin_type = :originType
        ORDER BY id
        """
    )
    fun get(originType: GifDataEntity.OriginType, title: String): PagingSource<Int, GifDataEntity>

    @Query("SELECT * FROM gif_data WHERE id = :id")
    fun getById(id: Int): Flow<GifDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gifData: List<GifDataEntity>)
}