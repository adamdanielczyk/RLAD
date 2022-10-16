package com.rlad.feature.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rlad.feature.search.R

/**
 * Simplified version of SearchBar implemented in https://github.com/SmartToolFactory/Jetpack-Compose-Tutorials
 */
@Composable
internal fun SearchBar(
    onQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit = {},
    onClearQueryClicked: () -> Unit,
    onBack: () -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var focused by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = focused) {
            IconButton(
                modifier = Modifier.padding(start = 2.dp),
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    query = ""
                    onBack()
                }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }

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
                query = ""
                onClearQueryClicked()
            },
            focused,
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onClearQueryClicked: () -> Unit,
    focused: Boolean,
    modifier: Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = modifier
            .then(
                Modifier
                    .height(56.dp)
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = if (focused) 0.dp else 16.dp,
                        end = 16.dp
                    )
            ),
        shape = CircleShape,
        elevation = 3.dp,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = modifier
            ) {
                val isQueryEmpty = query.isEmpty()
                if (isQueryEmpty) {
                    SearchHint(modifier.padding(start = 24.dp, end = 8.dp))
                }

                val focusManager = LocalFocusManager.current
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChanged,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .onFocusChanged {
                                onSearchFocusChanged(it.isFocused)
                            }
                            .focusRequester(focusRequester)
                            .padding(top = 9.dp, bottom = 8.dp, start = 24.dp, end = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions { focusManager.clearFocus() },
                        textStyle = TextStyle(MaterialTheme.colors.onSurface),
                        cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
                    )

                    if (!isQueryEmpty) {
                        IconButton(onClick = onClearQueryClicked) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                        }
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
            .then(modifier)

    ) {
        Text(
            text = stringResource(R.string.search_hint),
        )
    }
}
