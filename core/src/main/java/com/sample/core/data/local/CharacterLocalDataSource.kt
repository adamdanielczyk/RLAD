package com.sample.core.data.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterLocalDataSource @Inject constructor(private val characterDao: CharacterDao) {

    fun getCharactersBy(nameOrLocation: String): PagingSource<Int, CharacterEntity> =
        characterDao.getBy(nameOrLocation)

    fun getCharacterBy(id: Int): Flow<CharacterEntity> = characterDao.getBy(id)

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insert(characters)
    }
}