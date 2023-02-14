package com.dino.core.data.repository.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException

abstract class BasePagingSource<in T: Any, U: Any>: PagingSource<Int, U>() {
    companion object {
        const val PAGE_SIZE = 5
    }

    protected abstract val initialPage: Int

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, U> {
        return try {
            val page = params.key ?: initialPage
            val responseData = getResult(page, PAGE_SIZE)
            LoadResult.Page(
                data = responseData,
                prevKey = if(page == initialPage) null else page - 1,
                nextKey = if(responseData.size < PAGE_SIZE) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, U>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    abstract suspend fun getResult(page: Int, size: Int): List<U>
}