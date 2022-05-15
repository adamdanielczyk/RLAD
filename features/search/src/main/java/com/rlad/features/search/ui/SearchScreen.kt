package com.rlad.features.search.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rlad.domain.model.DataSourceUiModel
import com.rlad.domain.model.ItemUiModel
import com.rlad.features.search.R
import com.rlad.ui.defaultImageModel
import kotlinx.coroutines.flow.collectLatest
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

@OptIn(ExperimentalMaterialApi::class)
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
                FloatingActionButton(onClick = { showBottomSheet() }) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
                }
            }
        ) {
            Column {
                SearchBar(
                    onQueryTextChanged = viewModel::onQueryTextChanged,
                    onSearchFocused = viewModel::onSearchFocused,
                    onClearSearchClicked = viewModel::onClearSearchClicked,
                )
                ItemsList(viewModel, openDetails)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchBar(
    onQueryTextChanged: (String) -> Unit,
    onSearchFocused: () -> Unit,
    onClearSearchClicked: () -> Unit,
) {
    SearchBar(
        onQueryChanged = { newQuery -> onQueryTextChanged(newQuery) },
        onSearchFocusChanged = { focused ->
            if (focused) {
                onSearchFocused()
            }
        },
        onClearQueryClicked = { onClearSearchClicked() },
        onBack = { onClearSearchClicked() },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun ItemsList(
    viewModel: SearchViewModel,
    openDetails: (String) -> Unit,
) {
    val items = viewModel.itemsPagingData.collectAsState().value ?: return
    val lazyPagingItems = items.collectAsLazyPagingItems()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collectLatest {
            listState.animateScrollToItem(index = 0)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .map { offset -> offset > 0 }
            .filter { it }
            .collect {
                keyboardController?.hide()
            }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(
            isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
        ),
        onRefresh = lazyPagingItems::refresh,
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(8.dp),
            state = listState,
        ) {
            items(lazyPagingItems.itemCount) { index ->
                val item = lazyPagingItems[index] ?: return@items
                ItemCard(item, openDetails)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemCard(item: ItemUiModel, openDetails: (String) -> Unit) {
    Card(
        onClick = { openDetails(item.id) },
        elevation = 3.dp,
        modifier = Modifier.padding(8.dp),
    ) {
        Column {
            AsyncImage(
                model = defaultImageModel(context = LocalContext.current, imageUrl = item.imageUrl),
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
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                item.cardCaptions.forEach { caption ->
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }
        }
    }
}