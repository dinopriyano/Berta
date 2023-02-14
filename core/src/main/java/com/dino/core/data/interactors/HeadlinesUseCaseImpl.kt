package com.dino.core.data.interactors

import com.dino.core.data.source.remote.SafeApiCall
import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.domain.model.Resource
import com.dino.core.domain.repository.HeadlinesRepository
import com.dino.core.domain.use_case.HeadlinesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HeadlinesUseCaseImpl constructor(
  private val repository: HeadlinesRepository
): HeadlinesUseCase, SafeApiCall {
  override suspend fun getTopHeadlines(): Flow<Resource<NewsResponse>> {
    return flow {
      emit( safeApiCall { repository.getTopHeadlines() } )
    }.flowOn(Dispatchers.IO)
  }

}