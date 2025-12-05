package com.rlad.feature.details.ui

import android.app.Activity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.rlad.core.domain.model.ItemUiModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
internal fun DetailsScreen(id: String) {
    val context = LocalContext.current

    val viewModel = assistedMetroViewModel<DetailsViewModel, DetailsViewModel.Factory> {
        create(id)
    }

    val item = viewModel.item.collectAsState(initial = null).value ?: return

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window ?: return@DisposableEffect onDispose {}
        val insetsController = WindowCompat.getInsetsController(window, view)

        val originalStatusBarLight = insetsController.isAppearanceLightStatusBars

        WindowCompat.setDecorFitsSystemWindows(window, false)
        insetsController.isAppearanceLightStatusBars = false

        onDispose {
            insetsController.isAppearanceLightStatusBars = originalStatusBarLight
        }
    }

    DetailsScreenContent(
        onShareItemClicked = { viewModel.onShareItemClicked(context, item) },
        item = item,
    )
}

@Composable
private fun DetailsScreenContent(
    onShareItemClicked: () -> Unit,
    item: ItemUiModel,
) {
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(LocalDensity.current) { windowInfo.containerSize.width.toDp() }
    val heroHeight = screenWidth * 0.75f

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            HeroImage(
                imageUrl = item.imageUrl,
                height = heroHeight,
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset { IntOffset(0, -32.dp.roundToPx()) },
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            ) {
                MainContent(item = item)
            }
        }

        HeaderActionButtons(
            onBackClicked = { onBackPressedDispatcher?.onBackPressed() },
            onShareClicked = onShareItemClicked,
        )
    }
}

@Composable
private fun HeroImage(
    imageUrl: String,
    height: androidx.compose.ui.unit.Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    ) {
        AsyncImage(
            model = imageUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(alpha = 0.3f),
                            0.3f to Color.Transparent,
                            0.7f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.4f),
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun HeaderActionButtons(
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = { onBackClicked() },
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                    shape = CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
                contentDescription = null,
            )
        }

        IconButton(
            onClick = { onShareClicked() },
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                    shape = CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun MainContent(item: ItemUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                lineHeight = 38.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        item.detailsKeyValues.forEachIndexed { index, (title, text) ->
            if (text.isNotEmpty()) {
                DetailsSection(
                    title = title,
                    text = text,
                    showDivider = index < item.detailsKeyValues.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun DetailsSection(
    title: String,
    text: String,
    showDivider: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (showDivider) 20.dp else 0.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 22.sp,
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = if (showDivider) 20.dp else 0.dp),
        )

        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            )
        }
    }
}
