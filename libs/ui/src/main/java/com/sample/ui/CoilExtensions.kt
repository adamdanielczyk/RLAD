package com.sample.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Scale

fun defaultImageModel(
    context: Context,
    imageUrl: String,
) = ImageRequest.Builder(context)
    .data(imageUrl)
    .crossfade(enable = true)
    .placeholder(ColorDrawable(Color.LTGRAY))
    .fallback(ColorDrawable(Color.GRAY))
    .error(ColorDrawable(Color.RED))
    .scale(Scale.FILL)
    .decoderFactory(
        if (VERSION.SDK_INT >= 28) {
            ImageDecoderDecoder.Factory()
        } else {
            GifDecoder.Factory()
        }
    )
    .build()