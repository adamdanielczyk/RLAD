package com.rlad.core.infrastructure.artic.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticLocalDataSourceTest {

    private val artworks = listOf(
        ArtworkEntity(
            articId = 1,
            title = "title1",
            imageId = "img1",
            artistTitle = "artist1",
            artistDisplay = "display1",
            departmentTitle = "department1",
            placeOfOrigin = "place1",
            altText = "alt1",
            dateDisplay = "date1",
        ),
        ArtworkEntity(
            articId = 2,
            title = "title2",
            imageId = "img2",
            artistTitle = "artist2",
            artistDisplay = "display2",
            departmentTitle = "department2",
            placeOfOrigin = "place2",
            altText = "alt2",
            dateDisplay = "date2",
        )
    )

    private val fakeDao = FakeArtworkDao()
    private val localDataSource = ArticLocalDataSource(fakeDao)

    @Test
    fun getArtworkById_returnsStoredArtwork() = runTest {
        localDataSource.insert(artworks)
        assertEquals(
            artworks[1],
            localDataSource.getById("2").first()
        )
    }

    @Test
    fun getArtworkById_returnsNullWhenMissing() = runTest {
        localDataSource.insert(artworks)
        assertEquals(
            null,
            localDataSource.getById("10").first()
        )
    }

    private class FakeArtworkDao : ArtworkDao {
        private val storage = mutableListOf<ArtworkEntity>()

        override fun getAll() = object : androidx.paging.PagingSource<Int, ArtworkEntity>() {
            override fun getRefreshKey(state: androidx.paging.PagingState<Int, ArtworkEntity>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtworkEntity> =
                LoadResult.Page(storage, prevKey = null, nextKey = null)
        }

        override fun getById(articId: Int): Flow<ArtworkEntity?> = flow {
            emit(storage.firstOrNull { it.articId == articId })
        }

        override suspend fun insert(artworks: List<ArtworkEntity>) {
            storage.addAll(artworks)
        }

        override suspend fun clearTable() {
            storage.clear()
        }
    }
}
