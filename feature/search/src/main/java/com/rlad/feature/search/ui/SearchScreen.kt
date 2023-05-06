package com.rlad.feature.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rlad.core.domain.model.DataSourceUiModel
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.feature.search.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
internal fun SearchScreen(openDetails: (String) -> Unit) {
    val viewModel = hiltViewModel<SearchViewModel>()

    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colors.surface

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = surfaceColor,
        )
    }

    SearchScreenContent(viewModel, openDetails)
}

@Composable
private fun SearchScreenContent(
    viewModel: SearchViewModel,
    openDetails: (String) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    fun showBottomSheet() {
        coroutineScope.launch {
            bottomSheetState.show()
        }
    }

    fun hideBottomSheet() {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    val gridState = rememberLazyGridState()
    val isScrollToTopButtonVisible by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            SheetContent(
                availableDataSources = viewModel.getAvailableDataSourcesUseCase().collectAsState(initial = emptyList()).value,
                onDataSourceClicked = { dataSource ->
                    viewModel.onDataSourceClicked(dataSource)
                    hideBottomSheet()
                },
            )
        }
    ) {
        Scaffold(
            floatingActionButton = {
                Column {
                    AnimatedVisibility(
                        visible = isScrollToTopButtonVisible,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.padding(bottom = 8.dp),
                    ) {
                        FloatingActionButton(onClick = viewModel::onScrollToTopClicked) {
                            Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = null)
                        }
                    }

                    FloatingActionButton(onClick = ::showBottomSheet) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                SearchBar(
                    onQueryTextChanged = viewModel::onQueryTextChanged,
                    onClearSearchClicked = viewModel::onClearSearchClicked,
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    ItemsContainer(
                        viewModel = viewModel,
                        openDetails = openDetails,
                        gridState = gridState,
                    )
                }
            }
        }
    }
}

@Composable
private fun SheetContent(
    availableDataSources: List<DataSourceUiModel>,
    onDataSourceClicked: (DataSourceUiModel) -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 24.dp)) {
        Text(
            text = stringResource(R.string.data_source_picker_title),
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 16.dp,
            ),
            style = MaterialTheme.typography.h6,
        )

        availableDataSources.forEach { uiDataSource ->
            SheetItem(
                text = uiDataSource.pickerText,
                isSelected = uiDataSource.isSelected,
                onClicked = { onDataSourceClicked(uiDataSource) },
            )
        }
    }
}

@Composable
private fun SheetItem(
    text: String,
    isSelected: Boolean,
    onClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClicked)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth(),
    ) {
        if (isSelected) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Spacer(modifier = Modifier.width(32.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
private fun SearchBar(
    onQueryTextChanged: (String) -> Unit,
    onClearSearchClicked: () -> Unit,
) {
    SearchBar(
        onQueryChanged = { newQuery -> onQueryTextChanged(newQuery) },
        onClearQueryClicked = { onClearSearchClicked() },
        onBack = { onClearSearchClicked() },
    )
}

@Composable
private fun ItemsContainer(
    viewModel: SearchViewModel,
    openDetails: (String) -> Unit,
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
            PullRefreshWithGrid(viewModel, lazyPagingItems, gridState, openDetails)
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
        )
        Text(
            text = stringResource(R.string.search_no_results),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun PullRefreshWithGrid(
    viewModel: SearchViewModel,
    lazyPagingItems: LazyPagingItems<ItemUiModel>,
    gridState: LazyGridState,
    openDetails: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collectLatest {
            gridState.animateScrollToItem(index = 0)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    var previousIndex by remember {
        mutableStateOf(gridState.firstVisibleItemIndex)
    }

    var previousScrollOffset by remember {
        mutableStateOf(gridState.firstVisibleItemScrollOffset)
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

    val refreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = lazyPagingItems::refresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = refreshState),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(8.dp),
            state = gridState,
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id },
            ) { index ->
                val item = lazyPagingItems[index] ?: return@items
                ItemCard(item, openDetails)
            }

            if (!refreshing && lazyPagingItems.loadState.append is LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.onSurface)
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            scale = true,
        )
    }
}

@Composable
private fun ItemCard(item: ItemUiModel, openDetails: (String) -> Unit) {
    Card(
        onClick = { openDetails(item.id) },
        elevation = 3.dp,
        modifier = Modifier.padding(8.dp),
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h6,
                )

                item.cardCaptions.forEach { caption ->
                    if (caption.isEmpty()) return@forEach
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }
}