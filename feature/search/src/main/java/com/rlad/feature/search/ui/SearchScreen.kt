package com.rlad.feature.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rlad.core.domain.model.DataSourceUiModel
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.feature.search.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
internal fun SearchScreen(onItemCardClicked: (String) -> Unit) {
    val viewModel = hiltViewModel<SearchViewModel>()
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
            SearchBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)),
                onQueryTextChanged = viewModel::onQueryTextChanged,
                onClearSearchClicked = viewModel::onClearSearchClicked,
            )
        },
        floatingActionButton = {
            Column(
                modifier = Modifier.systemBarsPadding(),
            ) {
                AnimatedVisibility(
                    visible = isScrollToTopButtonVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    FloatingActionButton(onClick = viewModel::onScrollToTopClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = stringResource(R.string.cd_scroll_to_top),
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(R.string.cd_filter),
                    )
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
            windowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            SheetContent(
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
private fun SheetContent(
    availableDataSources: List<DataSourceUiModel>,
    onDataSourceClicked: (DataSourceUiModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.data_source_picker_title),
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 16.dp,
            ),
            style = MaterialTheme.typography.titleLarge,
        )

        availableDataSources.forEach { uiDataSource ->
            SheetItem(
                text = uiDataSource.pickerText,
                isSelected = uiDataSource.isSelected,
                onSheetItemClicked = { onDataSourceClicked(uiDataSource) },
            )
        }
    }
}

@Composable
private fun SheetItem(
    text: String,
    isSelected: Boolean,
    onSheetItemClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onSheetItemClicked)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth(),
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.cd_selected),
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Spacer(modifier = Modifier.width(32.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier,
    onQueryTextChanged: (String) -> Unit,
    onClearSearchClicked: () -> Unit,
) {
    SearchBar(
        modifier = modifier,
        onQueryChanged = onQueryTextChanged,
        onClearQueryClicked = onClearSearchClicked,
        onBackClicked = onClearSearchClicked,
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
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search_empty))
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(120.dp),
            contentDescription = stringResource(R.string.search_no_results),
        )
        Text(
            text = stringResource(R.string.search_no_results),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp),
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

    val pullToRefreshState = rememberPullToRefreshState()

    if (pullToRefreshState.isRefreshing) {
        lazyPagingItems.refresh()
    }

    LaunchedEffect(lazyPagingItems.loadState) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> Unit
            is LoadState.Error, is LoadState.NotLoading -> {
                pullToRefreshState.endRefresh()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        contentAlignment = Alignment.TopCenter,
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

        PullToRefreshContainer(
            state = pullToRefreshState,
        )
    }
}

@Composable
private fun ItemCard(item: ItemUiModel, onItemCardClicked: (String) -> Unit) {
    Card(
        onClick = { onItemCardClicked(item.id) },
        modifier = Modifier.padding(8.dp),
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = item.cardCaption?.replace("\n", " ").orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
