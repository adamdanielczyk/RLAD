package com.sample.core.data.repository

import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.remote.CharacterRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val localDataSource: CharacterLocalDataSource,
    private val remoteDataSource: CharacterRemoteDataSource
) {

    fun getAllCharacters(): Flow<List<CharacterEntity>> {
        return localDataSource.getCharacters().onEach { localCharacters ->
            if (localCharacters.isEmpty()) {
                val remoteCharacters = remoteDataSource.getAllCharacters().results
                val newLocalCharacters = remoteCharacters.map(::CharacterEntity)
                localDataSource.insertCharacters(newLocalCharacters)
            }
        }
    }

    fun getCharacterById(id: Int): Flow<CharacterEntity> {
        return localDataSource.getCharacterById(id)
    }
}