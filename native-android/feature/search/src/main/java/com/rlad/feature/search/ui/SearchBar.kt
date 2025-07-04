package com.rlad.feature.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rlad.feature.search.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Simplified version of SearchBar implemented in https://github.com/SmartToolFactory/Jetpack-Compose-Tutorials
 */
@Composable
internal fun SearchBar(
    modifier: Modifier,
    onQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit = {},
    onClearQueryClicked: () -> Unit,
    clearSearch: Flow<Unit>,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var focused by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun clearQuery() {
        query = ""
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(clearSearch) {
        clearSearch.collectLatest {
            clearQuery()
        }
    }

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchTextField(
            query,
            { newQuery ->
                query = newQuery
                onQueryChanged(newQuery)
            },
            { newFocused ->
                focused = newFocused
                onSearchFocusChanged(newFocused)
            },
            {
                clearQuery()
                onClearQueryClicked()
            },
            focused,
        )
    }
}

@Composable
private fun RowScope.SearchTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onClearQueryClicked: () -> Unit,
    focused: Boolean,
) {
    val focusRequester = remember { FocusRequester() }

    val borderColor by animateColorAsState(
        targetValue = if (focused) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(300),
    )

    val clearButtonAlpha by animateFloatAsState(
        targetValue = if (query.isNotEmpty()) 1f else 0f,
        animationSpec = tween(200),
    )

    Surface(
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            ),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(
            width = 2.dp,
            color = borderColor,
        ),
        shadowElevation = if (focused) 8.dp else 4.dp,
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
        ) {
            val isQueryEmpty = query.isEmpty()
            if (isQueryEmpty) {
                SearchHint(Modifier.padding(start = 20.dp, end = 8.dp))
            }

            val focusManager = LocalFocusManager.current
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChanged,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .wrapContentHeight(Alignment.CenterVertically)
                        .onFocusChanged {
                            onSearchFocusChanged(it.isFocused)
                        }
                        .focusRequester(focusRequester)
                        .padding(top = 8.dp, bottom = 8.dp, start = 20.dp, end = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions { focusManager.clearFocus() },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                )

                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    IconButton(
                        onClick = onClearQueryClicked,
                        modifier = Modifier.alpha(clearButtonAlpha),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHint(modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        Text(
            text = stringResource(R.string.search_hint),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
