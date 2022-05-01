package com.sample.features.details.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.sample.core.data.local.CharacterEntity
import com.sample.core.ui.defaultImageRequestBuilder
import com.sample.features.details.R

@Composable
fun DetailScreen() {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val character = viewModel.getCharacter().collectAsState(initial = null).value ?: return

    Scaffold(
        topBar = {
            val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopAppBar(
                title = {
                    Text(character.name)
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ImageWithGradient(character.imageUrl)

            Column(modifier = Modifier.padding(16.dp)) {
                DetailsText(
                    title = stringResource(R.string.details_status),
                    text = when (character.status) {
                        CharacterEntity.Status.ALIVE -> stringResource(R.string.status_alive)
                        CharacterEntity.Status.DEAD -> stringResource(R.string.status_dead)
                        CharacterEntity.Status.UNKNOWN -> stringResource(R.string.unknown)
                    }
                )

                DetailsText(
                    title = stringResource(R.string.details_species),
                    text = character.species
                )

                DetailsText(
                    title = stringResource(R.string.details_gender),
                    text = when (character.gender) {
                        CharacterEntity.Gender.FEMALE -> stringResource(R.string.gender_female)
                        CharacterEntity.Gender.MALE -> stringResource(R.string.gender_male)
                        CharacterEntity.Gender.GENDERLESS -> stringResource(R.string.gender_genderless)
                        CharacterEntity.Gender.UNKNOWN -> stringResource(R.string.unknown)
                    }
                )

                DetailsText(
                    title = stringResource(R.string.details_location),
                    text = character.location.name
                )
                DetailsText(
                    title = stringResource(R.string.details_created),
                    text = character.created
                )

                val type = character.type
                if (!type.isNullOrBlank()) {
                    DetailsText(
                        title = stringResource(R.string.details_type),
                        text = type
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageWithGradient(imageUrl: String) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
        startY = sizeImage.height.toFloat() / 3,
        endY = sizeImage.height.toFloat()
    )

    Box {
        Image(
            painter = rememberImagePainter(data = imageUrl, builder = defaultImageRequestBuilder()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .onGloballyPositioned {
                    sizeImage = it.size
                },
        )
        Box(modifier = Modifier
            .matchParentSize()
            .background(gradient))
    }
}

@Composable
private fun DetailsText(title: String, text: String) {
    Text(
        text = title,
        modifier = Modifier.padding(bottom = 4.dp),
        style = MaterialTheme.typography.h6,
    )
    Text(
        text = text,
        modifier = Modifier.padding(bottom = 8.dp),
        style = MaterialTheme.typography.body2,
    )
}