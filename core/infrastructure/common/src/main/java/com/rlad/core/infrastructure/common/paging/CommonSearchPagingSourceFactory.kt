package com.rlad.core.infrastructure.common.paging

import androidx.paging.PagingSource

interface CommonSearchPagingSourceFactory<RemoteModel : Any> {

    fun create(query: String): PagingSource<Int, RemoteModel>
}