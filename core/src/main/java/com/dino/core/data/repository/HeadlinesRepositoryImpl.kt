package com.dino.core.data.repository

import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.data.source.remote.service.HeadlinesService
import com.dino.core.domain.repository.HeadlinesRepository

class HeadlinesRepositoryImpl constructor(
  private val apiService: HeadlinesService
): HeadlinesRepository {
  override suspend fun getTopHeadlines(): NewsResponse {
    return apiService.getHeadlines()
  }
}