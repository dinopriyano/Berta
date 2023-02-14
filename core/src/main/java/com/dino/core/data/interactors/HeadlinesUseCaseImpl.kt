package com.dino.core.data.interactors

import androidx.paging.PagingData
import com.dino.core.data.repository.base.RemotePagingMapResult
import com.dino.core.data.source.remote.SafeApiCall
import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.domain.model.News
import com.dino.core.domain.repository.HeadlinesRepository
import com.dino.core.domain.use_case.HeadlinesUseCase
import de.yanneckreiss.kconmapper.generated.toNews
import kotlinx.coroutines.flow.Flow

class HeadlinesUseCaseImpl constructor(
  private val repository: HeadlinesRepository
): HeadlinesUseCase, SafeApiCall {
  override suspend fun getTopHeadlines(): Flow<PagingData<News>> {
    return object: RemotePagingMapResult<NewsResponse, News>() {
      override suspend fun fetchData(page: Int, size: Int): NewsResponse {
        return repository.getTopHeadlines()
      }

      override suspend fun mapData(data: NewsResponse): List<News> {
        return data.articles.map { it.toNews() }
      }
    }.getResultFlow()
  }

}