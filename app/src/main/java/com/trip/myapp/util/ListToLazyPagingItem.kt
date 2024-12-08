package com.trip.myapp.util

import androidx.compose.runtime.Composable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun <T : Any> List<T>.toLazyPagingItems(): LazyPagingItems<T> {
    val pager = Pager(PagingConfig(pageSize = this.size, initialLoadSize = this.size)) {
        object : PagingSource<Int, T>() {
            override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
                return LoadResult.Page(
                    data = this@toLazyPagingItems,
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }
    return pager.flow.collectAsLazyPagingItems()
}
