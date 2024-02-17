package com.rlad.core.infrastructure.rickandmorty.mapper

import android.app.Application
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.rickandmorty.R
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter
import javax.inject.Inject

internal class RickAndMortyModelMapper @Inject constructor(
    private val application: Application,
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
        cardCaption = local.species,
        detailsKeyValues = listOf(
            application.getString(R.string.details_name) to local.name,
            application.getString(R.string.details_status) to when (local.status) {
                CharacterEntity.Status.ALIVE -> application.getString(R.string.details_status_alive)
                CharacterEntity.Status.DEAD -> application.getString(R.string.details_status_dead)
                CharacterEntity.Status.UNKNOWN -> application.getString(R.string.details_status_unknown)
            },
            application.getString(R.string.details_species) to local.species,
            application.getString(R.string.details_gender) to when (local.gender) {
                CharacterEntity.Gender.FEMALE -> application.getString(R.string.details_gender_female)
                CharacterEntity.Gender.MALE -> application.getString(R.string.details_gender_male)
                CharacterEntity.Gender.GENDERLESS -> application.getString(R.string.details_gender_genderless)
                CharacterEntity.Gender.UNKNOWN -> application.getString(R.string.details_gender_unknown)
            },
            application.getString(R.string.details_location) to local.location.name,
            application.getString(R.string.details_created) to local.created,
        ) + listOfNotNull(
            if (!local.type.isNullOrBlank()) {
                application.getString(R.string.details_type) to local.type
            } else null
        ),
    )
}
