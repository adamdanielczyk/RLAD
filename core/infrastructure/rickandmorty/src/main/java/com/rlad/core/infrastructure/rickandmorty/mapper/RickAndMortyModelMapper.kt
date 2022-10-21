package com.rlad.core.infrastructure.rickandmorty.mapper

import android.content.Context
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.rickandmorty.R
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class RickAndMortyModelMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) : ModelMapper<CharacterEntity, ServerCharacter> {

    override fun toLocalModel(remote: ServerCharacter): CharacterEntity = CharacterEntity(
        id = remote.id,
        name = remote.name,
        status = when (remote.status) {
            ServerCharacter.Status.ALIVE -> CharacterEntity.Status.ALIVE
            ServerCharacter.Status.DEAD -> CharacterEntity.Status.DEAD
            ServerCharacter.Status.UNKNOWN -> CharacterEntity.Status.UNKNOWN
        },
        species = remote.species,
        type = remote.type,
        gender = when (remote.gender) {
            ServerCharacter.Gender.FEMALE -> CharacterEntity.Gender.FEMALE
            ServerCharacter.Gender.MALE -> CharacterEntity.Gender.MALE
            ServerCharacter.Gender.GENDERLESS -> CharacterEntity.Gender.GENDERLESS
            ServerCharacter.Gender.UNKNOWN -> CharacterEntity.Gender.UNKNOWN
        },
        location = CharacterEntity.Location(remote.location.name),
        imageUrl = remote.imageUrl,
        created = remote.created
    )

    override fun toUiModel(local: CharacterEntity): ItemUiModel = ItemUiModel(
        id = local.id.toString(),
        imageUrl = local.imageUrl,
        name = local.name,
        cardCaptions = listOf(
            local.species,
            local.location.name,
        ),
        detailsKeyValues = listOf(
            context.getString(R.string.details_status) to when (local.status) {
                CharacterEntity.Status.ALIVE -> context.getString(R.string.details_status_alive)
                CharacterEntity.Status.DEAD -> context.getString(R.string.details_status_dead)
                CharacterEntity.Status.UNKNOWN -> context.getString(R.string.details_status_unknown)
            },
            context.getString(R.string.details_species) to local.species,
            context.getString(R.string.details_gender) to when (local.gender) {
                CharacterEntity.Gender.FEMALE -> context.getString(R.string.details_gender_female)
                CharacterEntity.Gender.MALE -> context.getString(R.string.details_gender_male)
                CharacterEntity.Gender.GENDERLESS -> context.getString(R.string.details_gender_genderless)
                CharacterEntity.Gender.UNKNOWN -> context.getString(R.string.details_gender_unknown)
            },
            context.getString(R.string.details_location) to local.location.name,
            context.getString(R.string.details_created) to local.created,
        ) + listOfNotNull(
            if (!local.type.isNullOrBlank()) {
                context.getString(R.string.details_type) to local.type
            } else null
        ),
    )
}