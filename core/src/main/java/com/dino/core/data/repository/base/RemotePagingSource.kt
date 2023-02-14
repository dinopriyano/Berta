package com.dino.core.data.repository.base

abstract class RemotePagingSource<T: Any, U: Any>: BasePagingSource<T, U>() {

    override val initialPage: Int = 1

    override suspend fun getResult(page: Int, size: Int): List<U> {
        return mapData(fetchData(page, size))
    }

    protected abstract suspend fun fetchData(page: Int, size: Int): T

    protected abstract suspend fun mapData(data: T): List<U>
}