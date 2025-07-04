package com.rlad.composeapp.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rlad.composeapp.ui.components.DataSourceBottomSheet
import com.rlad.composeapp.ui.components.ItemCard
import com.rlad.composeapp.ui.components.SearchBar
import com.rlad.shared.domain.model.ItemUiModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onItemClick: (String) -> Unit = {},
    viewModel: SearchViewModel = koinViewModel()
) {
    var openBottomSheet by remember { mutableStateOf(false) }
    
    val gridState = rememberLazyGridState()
    val isScrollToTopButtonVisible by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }
    
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    onQueryChanged = viewModel::onQueryTextChanged,
                    onClearQueryClicked = viewModel::onClearSearchClicked,
                    clearSearch = viewModel.clearSearch,
                )
                
                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                    modifier = Modifier.size(52.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
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
            ) {
                FloatingActionButton(onClick = viewModel::onScrollToTopClicked) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            ItemsContainer(
                viewModel = viewModel,
                onItemCardClicked = onItemClick,
                gridState = gridState,
            )
        }
    }
    
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            DataSourceBottomSheet(
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
private fun ItemsContainer(
    viewModel: SearchViewModel,
    onItemCardClicked: (String) -> Unit,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
) {
    val itemsFlow = viewModel.itemsPagingData.collectAsState().value ?: return
    val items = itemsFlow.collectAsState(initial = emptyList()).value
    
    val isListEmpty by remember(items) {
        derivedStateOf {
            items.isEmpty()
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.scrollToTop.collectLatest {
            gridState.animateScrollToItem(index = 0)
        }
    }
    
    Crossfade(targetState = isListEmpty) { isListEmptyTargetState ->
        if (isListEmptyTargetState) {
            EmptyState()
        } else {
            ItemsGrid(
                items = items,
                gridState = gridState,
                onItemCardClicked = onItemCardClicked
            )
        }
    }
}

@Composable
private fun ItemsGrid(
    items: List<ItemUiModel>,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    onItemCardClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        state = gridState,
    ) {
        items(items) { item ->
            ItemCard(item, onItemCardClicked)
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
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No results found",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Try searching for something else",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}