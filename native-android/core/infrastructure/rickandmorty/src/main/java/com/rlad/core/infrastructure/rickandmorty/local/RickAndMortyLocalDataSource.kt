package com.rlad.core.infrastructure.rickandmorty.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class)
class RickAndMortyLocalDataSource(
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
