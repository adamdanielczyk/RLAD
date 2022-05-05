package com.rlad.infrastructure.giphy.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rlad.domain.model.ItemUiModel
import com.rlad.infrastructure.common.repository.ItemsRepository
import com.rlad.infrastructure.giphy.R
import com.rlad.infrastructure.giphy.local.GifDataEntity
import com.rlad.infrastructure.giphy.local.GiphyLocalDataSource
import com.rlad.infrastructure.giphy.paging.GiphyRemoteMediator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GiphyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataSource: GiphyLocalDataSource,
    private val remoteMediatorFactory: GiphyRemoteMediator.Factory,
) : ItemsRepository {

    override fun getDataSourceName(): String = "giphy"

    override fun getDataSourcePickerText(): String = context.getString(R.string.data_source_picker_giphy)

    override fun getItemById(id: String): Flow<ItemUiModel> {
        return localDataSource.getGifDataById(id).map { gifDataEntity -> gifDataEntity.toItemEntity() }
    }

    override fun getItems(query: String?): Flow<PagingData<ItemUiModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediatorFactory.create(query),
            pagingSourceFactory = { localDataSource.getGifDataByTitle(query.orEmpty()) }
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