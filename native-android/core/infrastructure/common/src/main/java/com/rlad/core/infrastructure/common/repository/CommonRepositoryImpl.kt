package com.rlad.core.infrastructure.common.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.paging.CommonRemoteMediator
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSource
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CommonRepository {
    fun getItemById(id: String): Flow<ItemUiModel>
    fun getAllItems(): Flow<PagingData<ItemUiModel>>
    fun getSearchItems(query: String): Flow<PagingData<ItemUiModel>>
}

class CommonRepositoryImpl<LocalModel : Any, RemoteModel : Any, RootRemoteData : Any>(
    private val localDataSource: CommonLocalDataSource<LocalModel>,
    private val remoteDataSource: CommonRemoteDataSource<RootRemoteData, RemoteModel>,
    private val remoteMediator: CommonRemoteMediator<LocalModel, RemoteModel, RootRemoteData>,
    private val searchPagingSourceFactory: CommonSearchPagingSource.Factory<RemoteModel, RootRemoteData>,
    private val modelMapper: ModelMapper<LocalModel, RemoteModel>,
) : CommonRepository {

    override fun getItemById(id: String): Flow<ItemUiModel> = localDataSource.getById(id).map { localModel ->
        if (localModel == null) {
            val remoteModel = remoteDataSource.getItem(id)
            modelMapper.toLocalModel(remoteModel)
        } else localModel
    }.map(modelMapper::toUiModel)

    override fun getAllItems(): Flow<PagingData<ItemUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = localDataSource::getAll,
        ).flow.map { pagingData -> pagingData.map(modelMapper::toUiModel) }
    }

    override fun getSearchItems(query: String): Flow<PagingData<ItemUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { searchPagingSourceFactory.create(query) },
        ).flow.map { pagingData ->
            pagingData
                .map(modelMapper::toLocalModel)
                .map(modelMapper::toUiModel)
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
