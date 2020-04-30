package com.sample.core.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.paging.CharactersBoundaryCallback
import com.sample.core.data.remote.CharacterRemoteDataSource
import com.sample.core.data.remote.ServerCharacter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val localDataSource: CharacterLocalDataSource,
    private val remoteDataSource: CharacterRemoteDataSource
) {

    fun getCharacterById(id: Int): Flow<CharacterEntity> = localDataSource.getCharacterById(id)

    fun getCharacters(
        scope: CoroutineScope,
        name: String? = null
    ): LiveData<PagedList<CharacterEntity>> {
        return localDataSource.getCharactersByName(name.orEmpty()).toLiveData(
            pageSize = 20,
            initialLoadKey = 1,
            boundaryCallback = CharactersBoundaryCallback(
                remoteDataSource,
                scope,
                name
            ) { serverCharacters -> insertCharacters(serverCharacters) }
        )
    }

    private suspend fun insertCharacters(serverCharacters: List<ServerCharacter>) {
        val newLocalCharacters = serverCharacters.map(::CharacterEntity)
        localDataSource.insertCharacters(newLocalCharacters)
    }
}