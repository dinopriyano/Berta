package com.dino.core.domain.use_case

import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface EverythingUseCase {
  suspend fun getEverythingNews(keyword: String): Flow<Resource<NewsResponse>>
}