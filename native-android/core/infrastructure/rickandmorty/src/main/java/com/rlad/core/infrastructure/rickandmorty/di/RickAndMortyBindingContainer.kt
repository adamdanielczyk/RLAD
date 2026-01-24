package com.rlad.core.infrastructure.rickandmorty.di

import android.app.Application
import androidx.room.Room
import com.rlad.core.infrastructure.rickandmorty.local.CharacterDao
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Qualifier
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@BindingContainer
@ContributesTo(AppScope::class)
object RickAndMortyBindingContainer {

    @Qualifier
    annotation class RickAndMortyHttpClient

    @Provides
    fun database(application: Application): RickAndMortyDatabase = Room.databaseBuilder(
        context = application,
        klass = RickAndMortyDatabase::class.java,
        name = "rickandmorty_database",
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun dao(database: RickAndMortyDatabase): CharacterDao = database.characterDao()

    @Provides
    @RickAndMortyHttpClient
    fun httpClient(): HttpClient = HttpClient(Android) {
        defaultRequest {
            url("https://rickandmortyapi.com")
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }
}
