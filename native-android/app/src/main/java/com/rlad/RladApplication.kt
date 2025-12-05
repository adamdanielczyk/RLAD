package com.rlad

import android.app.Application
import android.graphics.Color
import android.os.Build
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.rlad.di.AppGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

internal class RladApplication : Application(), ImageLoaderFactory, MetroApplication {

    private val appGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(enable = true)
            .placeholder(Color.LTGRAY.toDrawable())
            .fallback(Color.GRAY.toDrawable())
            .error(Color.RED.toDrawable())
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
}
