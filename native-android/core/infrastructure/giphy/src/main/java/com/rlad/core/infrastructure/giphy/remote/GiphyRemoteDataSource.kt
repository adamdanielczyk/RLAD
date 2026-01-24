package com.rlad.core.infrastructure.giphy.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding

@ContributesBinding(AppScope::class)
class GiphyRemoteDataSource(
    private val giphyApi: GiphyApi,
) : CommonRemoteDataSource<ServerGifsRoot, ServerGif> {

    override suspend fun getRootData(offset: Int, pageSize: Int): ServerGifsRoot = giphyApi.trendingGifs(
        offset = offset,
        limit = pageSize,
    )

    override fun getItems(rootData: ServerGifsRoot): List<ServerGif> = rootData.data

    override suspend fun getItem(id: String): ServerGif = giphyApi.getGif(
        gifId = id,
    ).data

    override fun getNextPagingOffset(rootData: ServerGifsRoot, currentlyLoadedPage: Int): Int {
        val pagination = rootData.pagination
        return pagination.offset + pagination.count
    }

    override fun getInitialPagingOffset(): Int = 0

    override suspend fun search(query: String, offset: Int, pageSize: Int): ServerGifsRoot = giphyApi.searchGifs(
        offset = offset,
        limit = pageSize,
        query = query,
    )
}
