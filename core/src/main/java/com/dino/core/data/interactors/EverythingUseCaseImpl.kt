package com.dino.core.data.interactors

import androidx.paging.PagingData
import com.dino.core.data.repository.base.RemotePagingMapResult
import com.dino.core.data.source.remote.SafeApiCall
import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.domain.model.News
import com.dino.core.domain.repository.EverythingRepository
import com.dino.core.domain.use_case.EverythingUseCase
import com.dino.core.utils.Constants
import de.yanneckreiss.kconmapper.generated.toNews
import kotlinx.coroutines.flow.Flow

class EverythingUseCaseImpl constructor(
  private val repository: EverythingRepository
): EverythingUseCase, SafeApiCall {
  override suspend fun getEverythingNews(keyword: String): Flow<PagingData<News>> {
    return object: RemotePagingMapResult<NewsResponse, News>() {
      override suspend fun fetchData(page: Int, size: Int): NewsResponse {
        return repository.getEverythingNews(keyword.ifEmpty { Constants.DEFAULT_KEYWORD })
      }

      override suspend fun mapData(data: NewsResponse): List<News> {
        return data.articles.map { it.toNews() }
      }

    }.getResultFlow()
  }

}