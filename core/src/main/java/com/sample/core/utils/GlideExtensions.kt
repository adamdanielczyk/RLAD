package com.sample.core.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun RequestBuilder<Drawable>.applyDefaults(): RequestBuilder<Drawable> {
    return transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(ColorDrawable(Color.LTGRAY))
        .fallback(ColorDrawable(Color.GRAY))
        .error(ColorDrawable(Color.RED))
}
