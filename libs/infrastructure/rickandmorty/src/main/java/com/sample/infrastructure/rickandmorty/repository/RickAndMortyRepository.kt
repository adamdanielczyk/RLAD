package com.sample.infrastructure.rickandmorty.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sample.domain.model.ItemUiModel
import com.sample.infrastructure.common.repository.ItemsRepository
import com.sample.infrastructure.rickandmorty.R
import com.sample.infrastructure.rickandmorty.local.CharacterEntity
import com.sample.infrastructure.rickandmorty.local.CharacterEntity.Gender
import com.sample.infrastructure.rickandmorty.local.CharacterEntity.Status
import com.sample.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.sample.infrastructure.rickandmorty.paging.CharacterRemoteMediator
import com.sample.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RickAndMortyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataSource: RickAndMortyLocalDataSource,
    private val remoteDataSource: RickAndMortyRemoteDataSource,
) : ItemsRepository {

    override fun getDataSourceName(): String = "rickandmorty"

    override fun getDataSourcePickerTextResId(): Int = R.string.data_source_picker_rickandmorty

    override fun getItemById(id: String): Flow<ItemUiModel> = localDataSource.getCharacterBy(id.toInt()).map { characterEntity -> characterEntity.toItemEntity() }

    override fun getItems(name: String?): Flow<PagingData<ItemUiModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                localDataSource = localDataSource,
                remoteDataSource = remoteDataSource,
                name = name
            ),
            pagingSourceFactory = { localDataSource.getCharactersBy(name.orEmpty()) }
        ).flow.map { pagingData -> pagingData.map { characterEntity -> characterEntity.toItemEntity() } }
    }

    private fun CharacterEntity.toItemEntity() = ItemUiModel(
        id = id.toString(),
        imageUrl = imageUrl,
        name = name,
        cardCaptions = listOf(
            species,
            location.name,
        ),
        detailsKeyValues = listOf(
            context.getString(R.string.details_status) to when (status) {
                Status.ALIVE -> context.getString(R.string.status_alive)
                Status.DEAD -> context.getString(R.string.status_dead)
                Status.UNKNOWN -> context.getString(R.string.unknown)
            },
            context.getString(R.string.details_species) to species,
            context.getString(R.string.details_gender) to when (gender) {
                Gender.FEMALE -> context.getString(R.string.gender_female)
                Gender.MALE -> context.getString(R.string.gender_male)
                Gender.GENDERLESS -> context.getString(R.string.gender_genderless)
                Gender.UNKNOWN -> context.getString(R.string.unknown)
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