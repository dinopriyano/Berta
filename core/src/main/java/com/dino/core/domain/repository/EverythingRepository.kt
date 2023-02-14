package com.dino.core.domain.repository

import com.dino.core.data.source.remote.dto.response.NewsResponse

interface EverythingRepository {
  suspend fun getEverythingNews(keyword: String): NewsResponse
}