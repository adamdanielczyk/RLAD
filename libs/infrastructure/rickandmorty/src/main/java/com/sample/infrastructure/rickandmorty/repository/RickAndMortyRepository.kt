package com.sample.infrastructure.rickandmorty.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.ItemsRepository
import com.sample.infrastructure.rickandmorty.R
import com.sample.infrastructure.rickandmorty.local.CharacterEntity
import com.sample.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.sample.infrastructure.rickandmorty.paging.CharacterRemoteMediator
import com.sample.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RickAndMortyRepository @Inject constructor(
    private val localDataSource: RickAndMortyLocalDataSource,
    private val remoteDataSource: RickAndMortyRemoteDataSource,
) : ItemsRepository {

    override fun getItemBy(id: String): Flow<ItemUiModel> = localDataSource.getCharacterBy(id.toInt()).map { characterEntity -> characterEntity.toItemEntity() }

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

    private fun CharacterEntity.toItemEntity(): ItemUiModel {
        fun keyValue(
            key: @Composable () -> String,
            value: @Composable () -> String,
        ): Pair<@Composable () -> String, @Composable () -> String> = key to value
        return ItemUiModel(
            id = id.toString(),
            imageUrl = imageUrl,
            name = name,
            cardCaptions = listOf(
                species,
                location.name,
            ),
            detailsKeyValues = listOf(
                keyValue(
                    key = { stringResource(R.string.details_status) },
                    value = {
                        when (status) {
                            CharacterEntity.Status.ALIVE -> stringResource(R.string.status_alive)
                            CharacterEntity.Status.DEAD -> stringResource(R.string.status_dead)
                            CharacterEntity.Status.UNKNOWN -> stringResource(R.string.unknown)
                        }
                    }
                ),
                keyValue(
                    key = { stringResource(R.string.details_species) },
                    value = { species }
                ),
                keyValue(
                    key = { stringResource(R.string.details_gender) },
                    value = {
                        when (gender) {
                            CharacterEntity.Gender.FEMALE -> stringResource(R.string.gender_female)
                            CharacterEntity.Gender.MALE -> stringResource(R.string.gender_male)
                            CharacterEntity.Gender.GENDERLESS -> stringResource(R.string.gender_genderless)
                            CharacterEntity.Gender.UNKNOWN -> stringResource(R.string.unknown)
                        }
                    }
                ),
                keyValue(
                    key = { stringResource(R.string.details_location) },
                    value = { location.name }
                ),
                keyValue(
                    key = { stringResource(R.string.details_created) },
                    value = { created }
                )
            ) + listOfNotNull(
                if (!type.isNullOrBlank()) {
                    keyValue(
                        key = { stringResource(R.string.details_type) },
                        value = { type }
                    )
                } else null
            ),
        )
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}