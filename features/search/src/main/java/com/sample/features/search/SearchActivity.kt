package com.sample.features.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sample.core.actions.Actions
import com.sample.core.data.local.CharacterEntity
import com.sample.core.ui.SampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleTheme {
                SearchScreen()
            }
        }

        viewModel.openDetailsScreen
            .onEach { characterEntity ->
                val activity = this@SearchActivity
                startActivity(Actions.openDetailsIntent(activity, characterEntity.id))
            }.launchIn(lifecycleScope)
    }

    @Composable
    private fun SearchScreen() {
        val systemUiController = rememberSystemUiController()

        val surfaceColor = MaterialTheme.colors.surface
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = surfaceColor,
            )
        }

        Scaffold {
            Column {
                SearchBar()
                CharactersList()
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun SearchBar() {
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
    private fun CharactersList() {
        val characters = viewModel.charactersPagingData.collectAsState(initial = null).value ?: return
        val lazyPagingCharacters = characters.collectAsLazyPagingItems()

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
            items(lazyPagingCharacters.itemCount) { index ->
                val character = lazyPagingCharacters[index] ?: return@items
                CharacterCard(character)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CharacterCard(character: CharacterEntity) {
        Card(
            onClick = { viewModel.onItemClicked(character) },
            elevation = 3.dp,
            modifier = Modifier.padding(8.dp),
        ) {
            Column {
                Image(
                    painter = rememberImagePainter(character.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Text(
                        text = character.species,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Text(
                        text = character.location.name,
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
    }
}
