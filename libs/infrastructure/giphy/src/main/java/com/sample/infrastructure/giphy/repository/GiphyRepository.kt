package com.sample.infrastructure.giphy.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.ItemsRepository
import com.sample.infrastructure.giphy.R
import com.sample.infrastructure.giphy.local.GifDataEntity
import com.sample.infrastructure.giphy.local.GiphyLocalDataSource
import com.sample.infrastructure.giphy.paging.GiphyRemoteMediator
import com.sample.infrastructure.giphy.remote.GiphyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GiphyRepository @Inject constructor(
    private val localDataSource: GiphyLocalDataSource,
    private val remoteDataSource: GiphyRemoteDataSource,
) : ItemsRepository {

    override fun getDataSourceName(): String = "giphy"

    override fun getDataSourcePickerTextResId(): Int = R.string.data_source_picker_giphy

    override fun getItemById(id: String): Flow<ItemUiModel> {
        return localDataSource.getGifDataById(id).map { gifDataEntity -> gifDataEntity.toItemEntity() }
    }

    override fun getItems(name: String?): Flow<PagingData<ItemUiModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GiphyRemoteMediator(
                localDataSource = localDataSource,
                remoteDataSource = remoteDataSource,
            ),
            pagingSourceFactory = { localDataSource.getGifDataByTitle(name.orEmpty()) }
        ).flow.map { pagingData -> pagingData.map { gifDataEntity -> gifDataEntity.toItemEntity() } }
    }

    private fun GifDataEntity.toItemEntity() = ItemUiModel(
        id = id,
        imageUrl = imageUrl,
        name = title,
        cardCaptions = emptyList(),
        detailsKeyValues = emptyList(),
    )

    companion object {
        const val PAGE_SIZE = 20
    }
}