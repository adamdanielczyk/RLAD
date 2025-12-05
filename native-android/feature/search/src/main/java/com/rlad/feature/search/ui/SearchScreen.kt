package com.rlad.feature.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.feature.search.R
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
internal fun SearchScreen(onItemCardClicked: (String) -> Unit) {
    val viewModel = metroViewModel<SearchViewModel>()
    SearchScreenContent(viewModel, onItemCardClicked)
}

@Composable
private fun SearchScreenContent(
    viewModel: SearchViewModel,
    onItemCardClicked: (String) -> Unit,
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val gridState = rememberLazyGridState()
    val isScrollToTopButtonVisible by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    onQueryTextChanged = viewModel::onQueryTextChanged,
                    onClearSearchClicked = viewModel::onClearSearchClicked,
                    clearSearch = viewModel.clearSearch,
                )

                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                    modifier = Modifier.size(52.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                    )
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isScrollToTopButtonVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.systemBarsPadding(),
            ) {
                FloatingActionButton(onClick = viewModel::onScrollToTopClicked) {
                    Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = null)
                }
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            ItemsContainer(
                viewModel = viewModel,
                onItemCardClicked = onItemCardClicked,
                gridState = gridState,
            )
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            DataSourceBottomSheetContent(
                availableDataSources = viewModel.getAvailableDataSourcesUseCase().collectAsState(initial = emptyList()).value,
                onDataSourceClicked = { dataSource ->
                    viewModel.onDataSourceClicked(dataSource)
                    openBottomSheet = false
                },
            )
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier,
    onQueryTextChanged: (String) -> Unit,
    onClearSearchClicked: () -> Unit,
    clearSearch: Flow<Unit>,
) {
    SearchBar(
        modifier = modifier,
        onQueryChanged = onQueryTextChanged,
        onClearQueryClicked = onClearSearchClicked,
        clearSearch = clearSearch,
    )
}

@Composable
private fun ItemsContainer(
    viewModel: SearchViewModel,
    onItemCardClicked: (String) -> Unit,
    gridState: LazyGridState,
) {
    val items = viewModel.itemsPagingData.collectAsState().value ?: return
    val lazyPagingItems = items.collectAsLazyPagingItems()

    val isListEmpty by remember(lazyPagingItems) {
        derivedStateOf {
            val append = lazyPagingItems.loadState.append
            append is LoadState.NotLoading && append.endOfPaginationReached && lazyPagingItems.itemCount == 0
        }
    }

    Crossfade(targetState = isListEmpty) { isListEmptyTargetState ->
        if (isListEmptyTargetState) {
            EmptyState()
        } else {
            PullRefreshWithGrid(viewModel, lazyPagingItems, gridState, onItemCardClicked)
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search_empty))
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(120.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.search_no_results_title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.search_no_results_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PullRefreshWithGrid(
    viewModel: SearchViewModel,
    lazyPagingItems: LazyPagingItems<ItemUiModel>,
    gridState: LazyGridState,
    onItemCardClicked: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collectLatest {
            gridState.animateScrollToItem(index = 0)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    var previousIndex by remember {
        mutableIntStateOf(gridState.firstVisibleItemIndex)
    }

    var previousScrollOffset by remember {
        mutableIntStateOf(gridState.firstVisibleItemScrollOffset)
    }

    LaunchedEffect(gridState) {
        combine(
            snapshotFlow { gridState.firstVisibleItemIndex },
            snapshotFlow { gridState.firstVisibleItemScrollOffset },
        ) { index, offset ->
            index to offset
        }
            .map { (newIndex, newOffset) ->
                val isScrollingDown = if (newIndex != previousIndex) {
                    newIndex > previousIndex
                } else {
                    newOffset > previousScrollOffset
                }
                previousIndex = newIndex
                previousScrollOffset = newOffset
                isScrollingDown
            }
            .filter { it }
            .collect {
                keyboardController?.hide()
            }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
        onRefresh = lazyPagingItems::refresh,
        isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount > 0,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = WindowInsets
                .navigationBars
                .add(WindowInsets(left = 8.dp, top = 8.dp, right = 8.dp, bottom = 8.dp))
                .asPaddingValues(),
            state = gridState,
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id },
            ) { index ->
                val item = lazyPagingItems[index] ?: return@items
                ItemCard(item, onItemCardClicked)
            }

            if (lazyPagingItems.loadState.source.refresh is LoadState.Loading || lazyPagingItems.loadState.append is LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemCard(item: ItemUiModel, onItemCardClicked: (String) -> Unit) {
    Card(
        onClick = { onItemCardClicked(item.id) },
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column {
            Box {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.2f),
                                ),
                            ),
                        ),
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp,
                    ),
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = item.cardCaption?.replace("\n", " ").orEmpty(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 18.sp,
                    ),
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
