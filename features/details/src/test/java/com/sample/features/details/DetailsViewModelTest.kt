package com.sample.features.details

import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterEntity.*
import com.sample.core.data.repository.CharacterRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsViewModelTest {

    private val characterRepository = mockk<CharacterRepository>()

    @Test
    fun getCharacter_returnMatchingRepositoryCharacter() = runBlockingTest {
        val character = CharacterEntity(
            id = 1,
            name = "Morty Smith",
            status = Status.ALIVE,
            species = "species 1",
            type = "type 1",
            gender = Gender.MALE,
            location = Location("location 1"),
            imageUrl = "url 1",
            created = "created 1"
        )
        every { characterRepository.getCharacterBy(id = 1) } returns flowOf(character)

        val viewModel = DetailsViewModel(
            characterId = 1,
            repository = characterRepository
        )

        assertEquals(character, viewModel.character.first())
    }
}