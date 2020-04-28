package com.sample.core.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterLocalDataSource @Inject constructor(private val characterDao: CharacterDao) {

    fun getCharacters(): Flow<List<CharacterEntity>> = characterDao.getAll()

    fun getCharacterById(id: Int): Flow<CharacterEntity> = characterDao.getById(id)

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insert(characters)
    }
}