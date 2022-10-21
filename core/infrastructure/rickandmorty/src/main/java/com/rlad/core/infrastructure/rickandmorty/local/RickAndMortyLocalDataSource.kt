package com.rlad.core.infrastructure.rickandmorty.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RickAndMortyLocalDataSource @Inject constructor(
    private val characterDao: CharacterDao,
) : CommonLocalDataSource<CharacterEntity> {

    override suspend fun insert(data: List<CharacterEntity>) {
        characterDao.insert(data)
    }

    override suspend fun clear() {
        characterDao.clearTable()
    }

    override fun getAll(): PagingSource<Int, CharacterEntity> = characterDao.getAll()

    override fun getById(id: String): Flow<CharacterEntity?> = characterDao.getById(id.toInt())
}