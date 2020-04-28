package com.sample.features.search.di

import androidx.lifecycle.ViewModelProvider
import com.sample.features.search.SearchActivity
import com.sample.features.search.SearchViewModel
import com.sample.features.search.SearchViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SearchModule(private val searchActivity: SearchActivity) {

    @Provides
    fun viewModel(searchViewModelFactory: SearchViewModelFactory): SearchViewModel {
        return ViewModelProvider(
            searchActivity,
            searchViewModelFactory
        ).get(SearchViewModel::class.java)
    }
}