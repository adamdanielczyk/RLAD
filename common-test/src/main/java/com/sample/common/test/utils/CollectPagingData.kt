package com.sample.common.test.utils

import androidx.paging.*
import kotlinx.coroutines.test.TestCoroutineDispatcher

private val differCallback = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
    val items = mutableListOf<T>()
    val differ = object : PagingDataDiffer<T>(differCallback, TestCoroutineDispatcher()) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            newCombinedLoadStates: CombinedLoadStates,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            for (idx in 0 until newList.size) {
                items.add(newList.getFromStorage(idx))
            }
            onListPresentable()
            return null
        }
    }
    differ.collectFrom(this)
    return items
}