package com.rlad.core.infrastructure.rickandmorty.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import javax.inject.Inject

internal class RickAndMortyRemoteDataSource @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
) : CommonRemoteDataSource<ServerGetCharacters, ServerCharacter> {

    override suspend fun getRootData(offset: Int): ServerGetCharacters = rickAndMortyApi.getCharacters(
        page = offset,
        name = null,
    )

    override fun getItems(rootData: ServerGetCharacters): List<ServerCharacter> = rootData.results

    override suspend fun getItem(id: String): ServerCharacter = rickAndMortyApi.getCharacter(id.toInt())

    override fun getNextPagingOffset(rootData: ServerGetCharacters, currentlyLoadedOffset: Int): Int = currentlyLoadedOffset + 1

    override fun getInitialPagingOffset(): Int = INITIAL_PAGING_KEY

    override fun getPageSize(): Int = PAGE_SIZE

    suspend fun getCharacters(page: Int, name: String?): List<ServerCharacter> = rickAndMortyApi.getCharacters(page, name).results

    companion object {
        const val INITIAL_PAGING_KEY = 1
        const val PAGE_SIZE = 20
    }
}