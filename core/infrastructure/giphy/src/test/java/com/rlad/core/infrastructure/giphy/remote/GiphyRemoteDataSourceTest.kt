package com.rlad.core.infrastructure.giphy.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GiphyRemoteDataSourceTest {

    private val gifs = listOf(
        ServerGif(
            id = "1",
            url = "url1",
            username = "user1",
            title = "title1",
            importDatetime = "import1",
            trendingDatetime = "trending1",
            images = ServerGif.Images(ServerGif.Images.Image(url = "image1")),
        ),
        ServerGif(
            id = "2",
            url = "url2",
            username = "user2",
            title = "cool",
            importDatetime = "import2",
            trendingDatetime = "trending2",
            images = ServerGif.Images(ServerGif.Images.Image(url = "image2")),
        )
    )

    private val fakeGiphyApi = FakeGiphyApi(gifs)
    private val remoteDataSource = GiphyRemoteDataSource(fakeGiphyApi)

    @Test
    fun trendingGifs_allApiItemsAreReturned() = runTest {
        assertEquals(
            ServerGifsRoot(
                data = gifs,
                pagination = ServerPagination(offset = 0, count = gifs.size)
            ),
            remoteDataSource.getRootData(offset = 0, pageSize = 10)
        )
    }

    @Test
    fun searchGifs_apiItemsAreFilteredByQuery() = runTest {
        assertEquals(
            ServerGifsRoot(
                data = listOf(gifs[1]),
                pagination = ServerPagination(offset = 0, count = 1)
            ),
            remoteDataSource.search(query = "cool", offset = 0, pageSize = 10)
        )
    }

    @Test
    fun getGif_itemWithMatchingIdIsReturned() = runTest {
        assertEquals(
            gifs[1],
            remoteDataSource.getItem(id = "2")
        )
    }

    private class FakeGiphyApi(
        private val gifs: List<ServerGif>
    ) : GiphyApi {
        override suspend fun trendingGifs(apiKey: String, offset: Int, limit: Int): ServerGifsRoot {
            return ServerGifsRoot(
                data = gifs,
                pagination = ServerPagination(offset = offset, count = gifs.size)
            )
        }

        override suspend fun searchGifs(apiKey: String, offset: Int, limit: Int, query: String): ServerGifsRoot {
            val filtered = gifs.filter { it.title.contains(query) }
            return ServerGifsRoot(
                data = filtered,
                pagination = ServerPagination(offset = offset, count = filtered.size)
            )
        }

        override suspend fun getGif(gifId: String, apiKey: String): ServerGifRoot {
            return ServerGifRoot(gifs.first { it.id == gifId })
        }
    }
}
