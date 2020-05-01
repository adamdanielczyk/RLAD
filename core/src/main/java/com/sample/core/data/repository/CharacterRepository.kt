package com.sample.core.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.paging.CharactersBoundaryCallback
import com.sample.core.data.remote.CharacterRemoteDataSource
import com.sample.core.data.remote.ServerCharacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val localDataSource: CharacterLocalDataSource,
    private val remoteDataSource: CharacterRemoteDataSource,
    private val diskExecutor: Executor,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getCharacterById(id: Int): Flow<CharacterEntity> = localDataSource.getCharacterById(id)

    fun getCharacters(
        scope: CoroutineScope,
        name: String? = null
    ): LiveData<PagedList<CharacterEntity>> {
        return localDataSource.getCharactersByName(name.orEmpty()).toLiveData(
            fetchExecutor = diskExecutor,
            pageSize = PAGE_SIZE,
            initialLoadKey = 1,
            boundaryCallback = CharactersBoundaryCallback(
                remoteDataSource,
                scope,
                ioDispatcher,
                name
            ) { serverCharacters -> insertCharacters(serverCharacters) }
        )
    }

    private suspend fun insertCharacters(serverCharacters: List<ServerCharacter>) {
        val newLocalCharacters = serverCharacters.map(::CharacterEntity)
        localDataSource.insertCharacters(newLocalCharacters)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}