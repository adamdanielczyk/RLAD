package com.rlad.core.infrastructure.artic.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import javax.inject.Inject

internal class ArticRemoteDataSource @Inject constructor(
    private val articApi: ArticApi,
) : CommonRemoteDataSource<ServerArtworksRoot, ServerArtwork> {

    override suspend fun getRootData(offset: Int): ServerArtworksRoot = articApi.artworks(
        from = offset,
        size = getPageSize(),
    )

    override fun getItems(rootData: ServerArtworksRoot): List<ServerArtwork> = rootData.data

    override suspend fun getItem(id: String): ServerArtwork = articApi.artwork(id).data

    override fun getNextPagingOffset(rootData: ServerArtworksRoot, currentlyLoadedOffset: Int): Int {
        val pagination = rootData.pagination
        return pagination.offset + pagination.limit
    }

    override fun getInitialPagingOffset(): Int = 0

    override fun getPageSize(): Int = 80

    suspend fun searchArtworks(
        query: String,
        from: Int,
        size: Int,
    ): ServerArtworksRoot = articApi.search(query, from, size)
}