package com.rlad.core.infrastructure.giphy.di

import android.content.Context
import androidx.room.Room
import com.rlad.core.infrastructure.giphy.local.GifDataDao
import com.rlad.core.infrastructure.giphy.local.GiphyDatabase
import com.rlad.core.infrastructure.giphy.remote.GiphyApi
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
    fun giphyApi(retrofit: Retrofit): GiphyApi =
        retrofit.create(GiphyApi::class.java)

    @Provides
    @GiphyScope
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.giphy.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}