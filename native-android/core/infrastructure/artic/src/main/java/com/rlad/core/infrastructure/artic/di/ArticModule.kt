package com.rlad.core.infrastructure.artic.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.artic.local.ArticDatabase
import com.rlad.core.infrastructure.artic.local.ArtworkDao
import com.rlad.core.infrastructure.artic.remote.ArticApi
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Qualifier
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Qualifier
annotation class ArticHttpClient

@BindingContainer
object ArticModule {

    @Provides
    fun database(application: Application): ArticDatabase = Room.databaseBuilder(
        context = application,
        klass = ArticDatabase::class.java,
        name = "artic_database",
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun dao(database: ArticDatabase): ArtworkDao = database.artworkDao()

    @Provides
    @ArticHttpClient
    fun httpClient(): HttpClient = HttpClient(Android) {
        defaultRequest {
            url("https://api.artic.edu")
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }

    @Provides
    fun api(@ArticHttpClient httpClient: HttpClient): ArticApi = ArticApi(httpClient)
}
