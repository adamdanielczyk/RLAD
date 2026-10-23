package com.rlad.core.infrastructure.common.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.common.local.AppPreferencesDao
import com.rlad.core.infrastructure.common.local.CommonDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object CommonBindingContainer {

    @Provides
    @SingleIn(AppScope::class)
    fun commonDatabase(application: Application): CommonDatabase = Room.databaseBuilder(
        application,
        CommonDatabase::class.java,
        "common_database",
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun appPreferencesDao(commonDatabase: CommonDatabase): AppPreferencesDao = commonDatabase.appPreferencesDao()
}
