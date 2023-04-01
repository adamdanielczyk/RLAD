package com.rlad.core.infrastructure.common.remote

interface CommonRemoteDataSource<RootRemoteData, RemoteModel> {

    suspend fun getRootData(offset: Int, pageSize: Int): RootRemoteData
    fun getItems(rootData: RootRemoteData): List<RemoteModel>
    suspend fun getItem(id: String): RemoteModel
    fun getNextPagingOffset(rootData: RootRemoteData, currentlyLoadedPage: Int): Int
    fun getInitialPagingOffset(): Int
    suspend fun search(query: String, offset: Int, pageSize: Int): RootRemoteData
}