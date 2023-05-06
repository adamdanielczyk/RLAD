package com.rlad.feature.details.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rlad.core.domain.model.ItemUiModel

@Composable
internal fun DetailsScreen() {
    val context = LocalContext.current

    val viewModel = hiltViewModel<DetailsViewModel>()
    val item = viewModel.item.collectAsState(initial = null).value ?: return

    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colors.primarySurface

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = surfaceColor,
        )
    }

    DetailsScreenContent(
        shareItemClicked = { viewModel.onShareItemClicked(context, item) },
        item = item,
    )
}

@Composable
private fun DetailsScreenContent(
    shareItemClicked: () -> Unit,
    item: ItemUiModel,
) {
    Scaffold(
        topBar = {
            val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopAppBar(
                title = {
                    Text(item.name)
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        IconButton(onClick = shareItemClicked) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            ImageWithGradient(item.imageUrl)

            Column(modifier = Modifier.padding(16.dp)) {
                item.detailsKeyValues.forEach { (title, text) ->
                    DetailsText(title, text)
                }
            }
        }
    }
}

@Composable
private fun ImageWithGradient(imageUrl: String) {
    var imageHeightY by remember { mutableStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
        startY = imageHeightY / 3,
        endY = imageHeightY
    )

    // calculate aspect ratio manually due to this issue https://issuetracker.google.com/issues/186012457
    val aspectRatioModifier = if (imageSize != Size.Zero) {
        Modifier.aspectRatio(imageSize.width / imageSize.height)
    } else {
        Modifier
    }

    Box {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            onSuccess = { state ->
                imageSize = state.painter.intrinsicSize
            },
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .then(aspectRatioModifier)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    imageHeightY = coordinates.size.height.toFloat()
                },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
    }
}

@Composable
private fun DetailsText(title: String, text: String) {
    if (text.isEmpty()) return
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