package com.rlad.core.infrastructure.giphy.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GiphyLocalDataSourceTest {

    private val gifs = listOf(
        GifEntity(
            giphyId = "1",
            title = "title1",
            imageUrl = "image1",
            username = "user1",
            importDatetime = "import1",
            trendingDatetime = "trend1",
        ),
        GifEntity(
            giphyId = "2",
            title = "title2",
            imageUrl = "image2",
            username = "user2",
            importDatetime = "import2",
            trendingDatetime = "trend2",
        )
    )

    private val fakeDao = FakeGifDao()
    private val localDataSource = GiphyLocalDataSource(fakeDao)

    @Test
    fun getGifById_returnsStoredGif() = runTest {
        localDataSource.insert(gifs)
        assertEquals(
            gifs[1],
            localDataSource.getById("2").first()
        )
    }

    @Test
    fun getGifById_returnsNullWhenMissing() = runTest {
        localDataSource.insert(gifs)
        assertEquals(
            null,
            localDataSource.getById("10").first()
        )
    }

    private class FakeGifDao : GifDao {
        private val storage = mutableListOf<GifEntity>()

        override fun getAll() = object : androidx.paging.PagingSource<Int, GifEntity>() {
            override fun getRefreshKey(state: androidx.paging.PagingState<Int, GifEntity>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifEntity> =
                LoadResult.Page(storage, prevKey = null, nextKey = null)
        }

        override fun getById(giphyId: String): Flow<GifEntity?> = flow {
            emit(storage.firstOrNull { it.giphyId == giphyId })
        }

        override suspend fun insert(gifs: List<GifEntity>) {
            storage.addAll(gifs)
        }

        override suspend fun clearTable() {
            storage.clear()
        }
    }
}
