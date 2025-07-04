package com.rlad.shared.di

import com.rlad.shared.data.artic.remote.ArticApi
import com.rlad.shared.data.giphy.remote.GiphyApi
import com.rlad.shared.data.rickandmorty.remote.RickAndMortyApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    
    single(named("rickandmorty")) {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest {
                url("https://rickandmortyapi.com/")
            }
        }
    }
    
    single(named("giphy")) {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest {
                url("https://api.giphy.com/")
            }
        }
    }
    
    single(named("artic")) {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest {
                url("https://api.artic.edu/")
            }
        }
    }
    
    // APIs
    single { RickAndMortyApi(get(named("rickandmorty"))) }
    single { GiphyApi(get(named("giphy")), "dc6zaTOxFJmzC") } // demo API key
    single { ArticApi(get(named("artic"))) }
}