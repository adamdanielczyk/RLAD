package com.sample.features.search.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sample.domain.model.ItemUiModel
import com.sample.ui.defaultImageModel
import kotlinx.coroutines.flow.collectLatest

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

    Scaffold {
        Column {
            SearchBar(viewModel)
            ItemsList(viewModel, openDetails)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchBar(viewModel: SearchViewModel) {
    SearchBar(
        onQueryChanged = { newQuery ->
            viewModel.onQueryTextChanged(newQuery)
        },
        onSearchFocusChanged = { focused ->
            if (focused) {
                viewModel.onSearchExpanded()
            } else {
                viewModel.onSearchCollapsed()
            }
        },
        onClearQueryClicked = {
            viewModel.onClearSearchClicked()
        },
        onBack = {
            viewModel.onClearSearchClicked()
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemsList(viewModel: SearchViewModel, openDetails: (String) -> Unit) {
    val items = viewModel.itemsPagingData.collectAsState(initial = null).value ?: return
    val lazyPagingItems = items.collectAsLazyPagingItems()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collectLatest {
            listState.animateScrollToItem(index = 0)
        }
    }

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