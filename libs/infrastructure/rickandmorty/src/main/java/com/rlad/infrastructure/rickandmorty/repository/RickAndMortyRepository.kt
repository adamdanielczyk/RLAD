package com.rlad.infrastructure.rickandmorty.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rlad.domain.model.ItemUiModel
import com.rlad.infrastructure.common.repository.ItemsRepository
import com.rlad.infrastructure.rickandmorty.R
import com.rlad.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.infrastructure.rickandmorty.local.CharacterEntity.Gender
import com.rlad.infrastructure.rickandmorty.local.CharacterEntity.Status
import com.rlad.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.rlad.infrastructure.rickandmorty.paging.RickAndMortyRemoteMediator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RickAndMortyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataSource: RickAndMortyLocalDataSource,
    private val remoteMediatorFactory: RickAndMortyRemoteMediator.Factory,
) : ItemsRepository {

    override fun getDataSourceName(): String = "rickandmorty"

    override fun getDataSourcePickerText(): String = context.getString(R.string.data_source_picker_rickandmorty)

    override fun getItemById(id: String): Flow<ItemUiModel> =
        localDataSource.getCharacterById(id.toInt()).map { characterEntity -> characterEntity.toUiModel() }

    override fun getItems(query: String?): Flow<PagingData<ItemUiModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediatorFactory.create(query),
            pagingSourceFactory = { localDataSource.getCharactersByName(query.orEmpty()) }
        ).flow.map { pagingData -> pagingData.map { characterEntity -> characterEntity.toUiModel() } }
    }

    private fun CharacterEntity.toUiModel() = ItemUiModel(
        id = id.toString(),
        imageUrl = imageUrl,
        name = name,
        cardCaptions = listOf(
            species,
            location.name,
        ),
        detailsKeyValues = listOf(
            context.getString(R.string.details_status) to when (status) {
                Status.ALIVE -> context.getString(R.string.details_status_alive)
                Status.DEAD -> context.getString(R.string.details_status_dead)
                Status.UNKNOWN -> context.getString(R.string.details_status_unknown)
            },
            context.getString(R.string.details_species) to species,
            context.getString(R.string.details_gender) to when (gender) {
                Gender.FEMALE -> context.getString(R.string.details_gender_female)
                Gender.MALE -> context.getString(R.string.details_gender_male)
                Gender.GENDERLESS -> context.getString(R.string.details_gender_genderless)
                Gender.UNKNOWN -> context.getString(R.string.details_gender_unknown)
            },
            context.getString(R.string.details_location) to location.name,
            context.getString(R.string.details_created) to created,
        ) + listOfNotNull(
            if (!type.isNullOrBlank()) {
                context.getString(R.string.details_type) to type
            } else null
        ),
    )

    companion object {
        const val PAGE_SIZE = 20
    }
}