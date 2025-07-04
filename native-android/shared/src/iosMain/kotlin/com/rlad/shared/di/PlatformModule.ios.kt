package com.rlad.shared.di

import com.rlad.shared.data.local.DatabaseFactory
import org.koin.dsl.module

val iosModule = module {
    single { DatabaseFactory() }
}