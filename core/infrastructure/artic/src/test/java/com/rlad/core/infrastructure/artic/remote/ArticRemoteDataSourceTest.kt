package com.rlad.core.infrastructure.artic.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticRemoteDataSourceTest {

    private val artworks = listOf(
        ServerArtwork(
            id = 1,
            title = "title1",
            imageId = "img1",
            thumbnail = ServerArtwork.Thumbnail(altText = "alt1"),
            artistTitle = "artist1",
            artistDisplay = "display1",
            placeOfOrigin = "place1",
            departmentTitle = "department1",
            dateDisplay = "date1",
        ),
        ServerArtwork(
            id = 2,
            title = "cool art",
            imageId = "img2",
            thumbnail = ServerArtwork.Thumbnail(altText = "alt2"),
            artistTitle = "artist2",
            artistDisplay = "display2",
            placeOfOrigin = "place2",
            departmentTitle = "department2",
            dateDisplay = "date2",
        )
    )

    private val fakeArticApi = FakeArticApi(artworks)
    private val remoteDataSource = ArticRemoteDataSource(fakeArticApi)

    @Test
    fun artworks_allApiItemsAreReturned() = runTest {
        assertEquals(
            ServerArtworksRoot(
                pagination = ServerArtworksRoot.ServerPagination(limit = artworks.size, offset = 0),
                data = artworks
            ),
            remoteDataSource.getRootData(offset = 0, pageSize = 10)
        )
    }

    @Test
    fun search_artworksAreFilteredByQuery() = runTest {
        assertEquals(
            ServerArtworksRoot(
                pagination = ServerArtworksRoot.ServerPagination(limit = 1, offset = 0),
                data = listOf(artworks[1])
            ),
            remoteDataSource.search(query = "cool", offset = 0, pageSize = 10)
        )
    }

    @Test
    fun artwork_itemWithMatchingIdIsReturned() = runTest {
        assertEquals(
            artworks[1],
            remoteDataSource.getItem(id = "2")
        )
    }

    private class FakeArticApi(
        private val artworks: List<ServerArtwork>
    ) : ArticApi {
        override suspend fun artworks(from: Int, size: Int): ServerArtworksRoot {
            return ServerArtworksRoot(
                pagination = ServerArtworksRoot.ServerPagination(limit = artworks.size, offset = from),
                data = artworks
            )
        }

        override suspend fun artwork(id: String): ServerArtworkRoot {
            return ServerArtworkRoot(artworks.first { it.id == id.toInt() })
        }

        override suspend fun search(query: String, from: Int, size: Int): ServerArtworksRoot {
            val filtered = artworks.filter { it.title.contains(query) }
            return ServerArtworksRoot(
                pagination = ServerArtworksRoot.ServerPagination(limit = filtered.size, offset = from),
                data = filtered
            )
        }
    }
}
