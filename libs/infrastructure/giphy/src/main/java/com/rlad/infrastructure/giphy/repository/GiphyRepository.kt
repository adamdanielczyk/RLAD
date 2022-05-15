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

    override fun getItemById(id: String): Flow<ItemUiModel> =
        localDataSource.getGifDataById(id).map { gifDataEntity -> gifDataEntity.toUiModel() }

    override fun getItems(query: String?): Flow<PagingData<ItemUiModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediatorFactory.create(query),
            pagingSourceFactory = {
                if (query != null) {
                    localDataSource.searchGifDataByTitle(query.orEmpty())
                } else {
                    localDataSource.getTrendingGifData()
                }
            }
        ).flow.map { pagingData -> pagingData.map { gifDataEntity -> gifDataEntity.toUiModel() } }
    }

    private fun GifDataEntity.toUiModel() = ItemUiModel(
        id = giphyId,
        imageUrl = imageUrl,
        name = title,
        cardCaptions = listOf(
            username,
        ),
        detailsKeyValues = listOf(
            context.getString(R.string.details_username) to username,
            context.getString(R.string.details_import_date) to importDatetime,
            context.getString(R.string.details_trending_date) to trendingDatetime,
        ),
    )

    companion object {
        const val PAGE_SIZE = 20
    }
}