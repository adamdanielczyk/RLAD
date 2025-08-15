package com.rlad.core.infrastructure.common.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.common.local.AppPreferencesDao
import com.rlad.core.infrastructure.common.local.CommonDatabase
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides

@BindingContainer
object CommonModule {

    @Provides
    fun commonDatabase(application: Application): CommonDatabase = Room.databaseBuilder(
        application,
        CommonDatabase::class.java,
        "common_database",
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun appPreferencesDao(commonDatabase: CommonDatabase): AppPreferencesDao = commonDatabase.appPreferencesDao()
}