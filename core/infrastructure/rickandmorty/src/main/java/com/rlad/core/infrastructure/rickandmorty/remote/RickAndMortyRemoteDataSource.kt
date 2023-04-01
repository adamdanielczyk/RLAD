package com.rlad.core.infrastructure.rickandmorty.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import javax.inject.Inject

internal class RickAndMortyRemoteDataSource @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
) : CommonRemoteDataSource<ServerGetCharacters, ServerCharacter> {

    override suspend fun getRootData(offset: Int, pageSize: Int): ServerGetCharacters = rickAndMortyApi.getCharacters(
        page = offset,
        name = null,
    )

    override fun getItems(rootData: ServerGetCharacters): List<ServerCharacter> = rootData.results

    override suspend fun getItem(id: String): ServerCharacter = rickAndMortyApi.getCharacter(id.toInt())

    override fun getNextPagingOffset(rootData: ServerGetCharacters, currentlyLoadedPage: Int): Int = currentlyLoadedPage + 1

    override fun getInitialPagingOffset(): Int = 1

    override suspend fun search(query: String, offset: Int, pageSize: Int): ServerGetCharacters = rickAndMortyApi.getCharacters(
        name = query,
        page = offset,
    )
}