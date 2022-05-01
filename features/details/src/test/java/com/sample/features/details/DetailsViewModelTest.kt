package com.sample.features.details

import androidx.lifecycle.SavedStateHandle
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterEntity.Gender
import com.sample.core.data.local.CharacterEntity.Location
import com.sample.core.data.local.CharacterEntity.Status
import com.sample.core.data.repository.CharacterRepository
import com.sample.features.details.ui.DetailsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsViewModelTest {

    private val characterRepository = mockk<CharacterRepository>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    @Test
    fun getCharacter_returnMatchingRepositoryCharacter() = runTest {
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
        every { savedStateHandle.get<String>("id") } returns "1"

        val viewModel = DetailsViewModel(characterRepository, savedStateHandle)

        assertEquals(character, viewModel.getCharacter().first())
    }
}