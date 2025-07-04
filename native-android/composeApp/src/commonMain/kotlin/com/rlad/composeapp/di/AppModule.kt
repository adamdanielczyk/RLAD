package com.rlad.composeapp.di

import com.rlad.composeapp.ui.details.DetailsViewModel
import com.rlad.composeapp.ui.search.SearchViewModel
import com.rlad.shared.di.networkModule
import com.rlad.shared.di.sharedModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: org.koin.core.module.Module

val appModule = module {
    includes(sharedModule, networkModule, platformModule)
    
    // ViewModels
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { (itemId: String) -> DetailsViewModel(itemId, get()) }
}