package com.rlad.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.rlad.core.infrastructure.artic.local.ArticDatabase
import com.rlad.core.infrastructure.common.local.CommonDatabase
import com.rlad.core.infrastructure.giphy.local.GiphyDatabase
import com.rlad.core.infrastructure.rickandmorty.local.RickAndMortyDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import org.junit.Assert.assertSame
import org.junit.Test

class DatabaseScopeTest {

    @Test
    fun databaseProviders_shareInstancesWithinApplicationGraph() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val graph = createGraphFactory<DatabaseTestGraph.Factory>().create(application)

        try {
            // Independent consumers must share Room's invalidation tracker so fetched rows
            // update active paging sources without restarting the app.
            assertSame(graph.articDatabase, graph.articDatabase)
            assertSame(graph.commonDatabase, graph.commonDatabase)
            assertSame(graph.giphyDatabase, graph.giphyDatabase)
            assertSame(graph.rickAndMortyDatabase, graph.rickAndMortyDatabase)
        } finally {
            graph.articDatabase.close()
            graph.commonDatabase.close()
            graph.giphyDatabase.close()
            graph.rickAndMortyDatabase.close()
        }
    }
}

@DependencyGraph(AppScope::class)
interface DatabaseTestGraph {
    val articDatabase: ArticDatabase
    val commonDatabase: CommonDatabase
    val giphyDatabase: GiphyDatabase
    val rickAndMortyDatabase: RickAndMortyDatabase

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): DatabaseTestGraph
    }
}
