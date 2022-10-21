package com.rlad.core.infrastructure.common.remote

interface CommonRemoteDataSource<RootRemoteData, RemoteModel> {

    suspend fun getRootData(offset: Int): RootRemoteData
    fun getItems(rootData: RootRemoteData): List<RemoteModel>
    suspend fun getItem(id: String): RemoteModel
    fun getNextPagingOffset(rootData: RootRemoteData, currentlyLoadedOffset: Int): Int
    fun getInitialPagingOffset(): Int
    fun getPageSize(): Int
}