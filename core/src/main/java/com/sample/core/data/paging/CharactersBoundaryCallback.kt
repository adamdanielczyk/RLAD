package com.sample.core.data.paging

import androidx.paging.PagedList
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.remote.CharacterRemoteDataSource
import com.sample.core.data.remote.ServerCharacter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharactersBoundaryCallback(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val scope: CoroutineScope,
    private val name: String? = null,
    private val insertCharacters: suspend (List<ServerCharacter>) -> Unit
) : PagedList.BoundaryCallback<CharacterEntity>() {

    /**
     * Store page key in memory.
     * For better experience, last loaded page key could be stored on disk.
     */
    private var nextKey = 1

    override fun onZeroItemsLoaded() {
        fetchAndSave()
    }

    override fun onItemAtEndLoaded(itemAtEnd: CharacterEntity) {
        fetchAndSave()
    }

    private fun fetchAndSave() {
        scope.launch(Dispatchers.IO) {
            try {
                val characters = remoteDataSource.getCharacters(
                    page = nextKey,
                    name = name
                )
                insertCharacters(characters)
                nextKey++
            } catch (ex: Exception) {
                // do nothing in case of network error, could be improved
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: CharacterEntity) {
        // ignored, items are only appended
    }
}