package com.dino.core.data.source.remote.service.impl

import com.dino.core.data.source.remote.HttpRoutes
import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.data.source.remote.service.HeadlinesService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class HeadlinesServiceImpl constructor(
  private val client: HttpClient
) : HeadlinesService {
  override suspend fun getHeadlines(): NewsResponse {
    return client.get {
      url(HttpRoutes.HEADLINES)
      parameter(HttpRoutes.PAGE_SIZE, HttpRoutes.DEFAULT_PAGE_SIZE)
      parameter(HttpRoutes.PAGE, HttpRoutes.DEFAULT_PAGE)
    }.body()
  }

}