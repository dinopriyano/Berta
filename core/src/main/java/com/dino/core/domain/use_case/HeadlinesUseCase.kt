package com.dino.core.domain.use_case

import androidx.paging.PagingData
import com.dino.core.domain.model.News
import kotlinx.coroutines.flow.Flow

interface HeadlinesUseCase {
  suspend fun getTopHeadlines(): Flow<PagingData<News>>
}