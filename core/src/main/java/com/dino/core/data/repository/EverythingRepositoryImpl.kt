package com.dino.core.data.repository

import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.data.source.remote.service.EverythingService
import com.dino.core.domain.repository.EverythingRepository

class EverythingRepositoryImpl constructor(
  private val apiService: EverythingService
): EverythingRepository {
  override suspend fun getEverythingNews(keyword: String): NewsResponse {
    return apiService.getEverything(keyword)
  }
}