package com.rlad

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.gif.AnimatedImageDecoder
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import com.rlad.di.AppGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

internal class RladApplication : Application(), SingletonImageLoader.Factory, MetroApplication {

    private val appGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(enable = true)
            .placeholder(Color.LTGRAY.toDrawable().asImage())
            .fallback(Color.GRAY.toDrawable().asImage())
            .error(Color.RED.toDrawable().asImage())
            .components {
                add(AnimatedImageDecoder.Factory())
                add(OkHttpNetworkFetcherFactory(cacheStrategy = { CacheControlCacheStrategy() }))
            }
            .build()
    }
}
