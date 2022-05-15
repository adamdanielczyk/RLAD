package com.rlad.infrastructure.giphy.di

import android.content.Context
import androidx.room.Room
import com.rlad.infrastructure.giphy.local.GifDataDao
import com.rlad.infrastructure.giphy.local.GiphyDatabase
import com.rlad.infrastructure.giphy.local.GiphyPreferencesDao
import com.rlad.infrastructure.giphy.remote.GiphyApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(GiphyComponent::class)
@Module
internal object GiphyModule {

    @Provides
    @GiphyScope
    fun giphyDatabase(@ApplicationContext context: Context): GiphyDatabase {
        return Room.databaseBuilder(
            context,
            GiphyDatabase::class.java,
            "giphy_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @GiphyScope
    fun gifDataDao(giphyDatabase: GiphyDatabase): GifDataDao =
        giphyDatabase.gifDataDao()

    @Provides
    @GiphyScope
    fun giphyPreferencesDao(giphyDatabase: GiphyDatabase): GiphyPreferencesDao =
        giphyDatabase.giphyPreferencesDao()

    @Provides
    @GiphyScope
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @GiphyScope
    fun giphyApi(retrofit: Retrofit): GiphyApi =
        retrofit.create(GiphyApi::class.java)

    @Provides
    @GiphyScope
    fun retrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.giphy.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}