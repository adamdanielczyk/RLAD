package com.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DaggerViewModelFactory @Inject constructor(
    private val viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModelProviders[modelClass]
            ?: error("Unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return viewModelProvider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}