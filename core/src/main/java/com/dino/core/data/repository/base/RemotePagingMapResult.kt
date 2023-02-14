package com.dino.core.data.repository.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

abstract class RemotePagingMapResult<Response: Any, Result: Any>: RemotePagingSource<Response, Result>() {

    private val pagingResult = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = {this}
    )

    fun getResultFlow() = pagingResult.flow.flowOn(Dispatchers.IO)

}