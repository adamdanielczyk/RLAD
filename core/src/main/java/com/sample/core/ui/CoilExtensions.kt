package com.sample.core.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.request.ImageRequest
import coil.size.Scale

fun defaultImageRequestBuilder(): ImageRequest.Builder.() -> Unit = {
    crossfade(true)
    placeholder(ColorDrawable(Color.LTGRAY))
    fallback(ColorDrawable(Color.GRAY))
    error(ColorDrawable(Color.RED))
    scale(Scale.FILL)
}