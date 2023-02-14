package com.dino.core.data.interactors

import com.dino.core.data.source.remote.SafeApiCall
import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.domain.model.Resource
import com.dino.core.domain.repository.EverythingRepository
import com.dino.core.domain.use_case.EverythingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EverythingUseCaseImpl constructor(
  private val repository: EverythingRepository
): EverythingUseCase, SafeApiCall {
  override suspend fun getEverythingNews(keyword: String): Flow<Resource<NewsResponse>> {
    return flow {
      emit( safeApiCall { repository.getEverythingNews(keyword) } )
    }.flowOn(Dispatchers.IO)
  }

}