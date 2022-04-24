package com.sample.features.details

import android.os.Bundle
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import coil.compose.rememberImagePainter
import com.sample.core.actions.EXTRA_CHARACTER_ID
import com.sample.core.data.local.CharacterEntity.Gender
import com.sample.core.data.local.CharacterEntity.Status
import com.sample.core.ui.SampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: DetailsViewModel.Factory

    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.provideFactory(
            factory = viewModelFactory,
            characterId = intent.extras?.getInt(EXTRA_CHARACTER_ID)!!
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                DetailScreen()
            }
        }
    }

    @Composable
    private fun DetailScreen() {
        val character = viewModel.character.collectAsState(initial = null).value ?: return

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
                            Status.ALIVE -> getString(R.string.status_alive)
                            Status.DEAD -> getString(R.string.status_dead)
                            Status.UNKNOWN -> getString(R.string.unknown)
                        }
                    )

                    DetailsText(
                        title = stringResource(R.string.details_species),
                        text = character.species
                    )

                    DetailsText(
                        title = stringResource(R.string.details_gender),
                        text = when (character.gender) {
                            Gender.FEMALE -> getString(R.string.gender_female)
                            Gender.MALE -> getString(R.string.gender_male)
                            Gender.GENDERLESS -> getString(R.string.gender_genderless)
                            Gender.UNKNOWN -> getString(R.string.unknown)
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
            colors = listOf(Color.Transparent, Color(0x80000000)),
            startY = sizeImage.height.toFloat() / 3,
            endY = sizeImage.height.toFloat()
        )

        Box {
            Image(
                painter = rememberImagePainter(imageUrl),
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
}