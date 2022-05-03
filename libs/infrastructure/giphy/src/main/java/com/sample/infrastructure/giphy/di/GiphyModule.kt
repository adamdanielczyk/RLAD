package com.sample.infrastructure.giphy.di

import android.content.Context
import androidx.room.Room
import com.sample.domain.repository.ItemsRepository
import com.sample.infrastructure.giphy.local.GifDataDao
import com.sample.infrastructure.giphy.local.GiphyDatabase
import com.sample.infrastructure.giphy.remote.GiphyApi
import com.sample.infrastructure.giphy.repository.GiphyRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [GiphyModule.Bindings::class])
internal class GiphyModule {

    @Provides
    @Singleton
    fun giphyDatabase(@ApplicationContext context: Context): GiphyDatabase {
        return Room.databaseBuilder(
            context,
            GiphyDatabase::class.java,
            "giphy_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun gifDataDao(giphyDatabase: GiphyDatabase): GifDataDao =
        giphyDatabase.gifDataDao()

    @Provides
    @Singleton
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun giphyApi(retrofit: Retrofit): GiphyApi =
        retrofit.create(GiphyApi::class.java)

    @Provides
    @Singleton
    fun retrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.giphy.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    internal interface Bindings {

        @Binds
        fun bindItemsRepository(impl: GiphyRepository): ItemsRepository
    }
}