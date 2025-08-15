package com.rlad

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.rlad.core.ui.di.AppGraph
import dev.zacsweers.metro.createGraph

internal class RladApplication : Application(), ImageLoaderFactory {
    
    val appGraph by lazy { createGraph<AppGraph>() }

    override fun onCreate() {
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(enable = true)
            .placeholder(ColorDrawable(Color.LTGRAY))
            .fallback(ColorDrawable(Color.GRAY))
            .error(ColorDrawable(Color.RED))
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