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

    fun getAllCharacters(scope: CoroutineScope): LiveData<PagedList<CharacterEntity>> {
        return localDataSource.getCharacters().toLiveData(
            pageSize = 20,
            initialLoadKey = 1,
            boundaryCallback = CharactersBoundaryCallback(
                remoteDataSource,
                scope
            ) { serverCharacters -> insertCharacters(serverCharacters) }
        )
    }

    private suspend fun insertCharacters(serverCharacters: List<ServerCharacter>) {
        val newLocalCharacters = serverCharacters.map { serverCharacter ->
            CharacterEntity(serverCharacter)
        }
        localDataSource.insertCharacters(newLocalCharacters)
    }

    fun getCharacterById(id: Int): Flow<CharacterEntity> {
        return localDataSource.getCharacterById(id)
    }
}