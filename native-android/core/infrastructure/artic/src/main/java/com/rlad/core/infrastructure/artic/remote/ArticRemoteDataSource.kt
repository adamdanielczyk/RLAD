package com.rlad.core.infrastructure.artic.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import javax.inject.Inject

internal class ArticRemoteDataSource @Inject constructor(
    private val articApi: ArticApi,
) : CommonRemoteDataSource<ServerArtworksRoot, ServerArtwork> {

    override suspend fun getRootData(offset: Int, pageSize: Int): ServerArtworksRoot = articApi.artworks(
        query = null,
        from = offset,
        size = pageSize,
    )

    override fun getItems(rootData: ServerArtworksRoot): List<ServerArtwork> = rootData.data

    override suspend fun getItem(id: String): ServerArtwork = articApi.artwork(id).data

    override fun getNextPagingOffset(rootData: ServerArtworksRoot, currentlyLoadedPage: Int): Int {
        val pagination = rootData.pagination
        return pagination.offset + pagination.limit
    }

    override fun getInitialPagingOffset(): Int = 0

    override suspend fun search(query: String, offset: Int, pageSize: Int): ServerArtworksRoot = articApi.artworks(
        query = query,
        from = offset,
        size = pageSize,
    )
}
