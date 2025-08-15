package com.rlad.core.infrastructure.giphy.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.giphy.local.GifDao
import com.rlad.core.infrastructure.giphy.local.GiphyDatabase
import com.rlad.core.infrastructure.giphy.remote.GiphyApi
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
annotation class GiphyHttpClient


@BindingContainer
object GiphyModule {

    @Provides
    fun database(application: Application): GiphyDatabase = Room.databaseBuilder(
        context = application,
        klass = GiphyDatabase::class.java,
        name = "giphy_database",
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun dao(database: GiphyDatabase): GifDao = database.gifDao()

    @Provides
    @GiphyHttpClient
    fun httpClient(): HttpClient = HttpClient(Android) {
        defaultRequest {
            url("https://api.giphy.com")
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
    fun api(@GiphyHttpClient httpClient: HttpClient): GiphyApi = GiphyApi(httpClient)
}
