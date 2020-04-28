package com.sample.core.data.di

import android.app.Application

interface CoreComponentProvider {

    fun provideCoreComponent(): CoreComponent
}

val Application.coreComponent: CoreComponent
    get() = (this as CoreComponentProvider).provideCoreComponent()