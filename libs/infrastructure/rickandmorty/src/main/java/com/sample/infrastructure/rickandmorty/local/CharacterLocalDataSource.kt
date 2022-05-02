package com.sample.infrastructure.rickandmorty.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CharacterLocalDataSource @Inject constructor(private val characterDao: CharacterDao) {

    fun getCharactersBy(nameOrLocation: String): PagingSource<Int, CharacterEntity> =
        characterDao.getBy(nameOrLocation)

    fun getCharacterBy(id: Int): Flow<CharacterEntity> = characterDao.getBy(id)

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insert(characters)
    }
}