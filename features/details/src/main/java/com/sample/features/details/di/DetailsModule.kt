package com.sample.features.details.di

import androidx.lifecycle.ViewModelProvider
import com.sample.features.details.DetailsActivity
import com.sample.features.details.DetailsViewModel
import com.sample.features.details.DetailsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class DetailsModule(private val detailsActivity: DetailsActivity) {

    @Provides
    fun viewModel(detailsViewModelFactory: DetailsViewModelFactory): DetailsViewModel {
        return ViewModelProvider(
            detailsActivity,
            detailsViewModelFactory
        ).get(DetailsViewModel::class.java)
    }
}