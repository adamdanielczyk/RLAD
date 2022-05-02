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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.sample.ui.defaultImageRequestBuilder

@Composable
internal fun DetailScreen() {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val item = viewModel.getItem().collectAsState(initial = null).value ?: return

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
            )
        },
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ImageWithGradient(item.imageUrl)

            Column(modifier = Modifier.padding(16.dp)) {
                item.detailsKeyValues.forEach { (title, text) ->
                    DetailsText(title = title(), text = text())
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