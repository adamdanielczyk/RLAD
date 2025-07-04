package com.rlad.composeapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.rlad.composeapp.ui.search.SearchScreen

@Composable
fun App() {
    MaterialTheme {
        SearchScreen()
    }
}