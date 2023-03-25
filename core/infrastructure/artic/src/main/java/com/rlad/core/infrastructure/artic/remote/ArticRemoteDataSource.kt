package com.rlad.core.infrastructure.artic.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import javax.inject.Inject

internal class ArticRemoteDataSource @Inject constructor(
    private val articApi: ArticApi,
) : CommonRemoteDataSource<ServerArtworks, ServerArtworkData> {

    override suspend fun getRootData(offset: Int): ServerArtworks = articApi.artworks(
        from = offset,
        size = getPageSize(),
    )

    override fun getItems(rootData: ServerArtworks): List<ServerArtworkData> = rootData.data

    override suspend fun getItem(id: String): ServerArtworkData = articApi.artwork(id).data

    override fun getNextPagingOffset(rootData: ServerArtworks, currentlyLoadedOffset: Int): Int {
        val pagination = rootData.pagination
        return pagination.offset + pagination.limit
    }

    override fun getInitialPagingOffset(): Int = 0

    override fun getPageSize(): Int = 80

    suspend fun searchArtworks(
        query: String,
        from: Int,
        size: Int,
    ): ServerArtworks = articApi.search(query, from, size)
}