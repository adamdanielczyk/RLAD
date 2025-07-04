package com.rlad.shared.data.common.remote

interface CommonRemoteDataSource<RootData : Any, ItemData : Any> {
    suspend fun getRootData(offset: Int, pageSize: Int): RootData
    fun getItems(rootData: RootData): List<ItemData>
    suspend fun getItem(id: String): ItemData
    fun getNextPagingOffset(rootData: RootData, currentlyLoadedPage: Int): Int
    fun getInitialPagingOffset(): Int
    suspend fun search(query: String, offset: Int, pageSize: Int): RootData
}