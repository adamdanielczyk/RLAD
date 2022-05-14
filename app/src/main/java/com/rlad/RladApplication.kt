package com.rlad

import android.app.Application
import com.rlad.domain.initializer.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
internal class RladApplication : Application() {

    @Inject lateinit var appInitializers: Set<@JvmSuppressWildcards AppInitializer>

    private val mainScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        mainScope.launch {
            appInitializers.forEach { appInitializer -> appInitializer.initialize() }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mainScope.cancel()
    }
}