package com.rlad.infrastructure.rickandmorty.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RickAndMortyLocalDataSource @Inject constructor(private val characterDao: CharacterDao) {

    fun getCharactersByName(name: String): PagingSource<Int, CharacterEntity> =
        characterDao.getByName(name)

    fun getCharacterById(id: Int): Flow<CharacterEntity> = characterDao.getById(id)

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insert(characters)
    }
}