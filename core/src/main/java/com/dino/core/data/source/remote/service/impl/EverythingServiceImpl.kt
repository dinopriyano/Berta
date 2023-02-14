package com.dino.core.data.source.remote.service.impl

import com.dino.core.data.source.remote.HttpRoutes
import com.dino.core.data.source.remote.dto.response.NewsResponse
import com.dino.core.data.source.remote.service.EverythingService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.parametersOf

class EverythingServiceImpl constructor(
  private val client: HttpClient
): EverythingService {
  override suspend fun getEverything(keyword: String): NewsResponse {
    return client.get {
      url(HttpRoutes.EVERYTHING)
      parameter(HttpRoutes.SEARCH_KEY, keyword)
      parameter(HttpRoutes.PAGE_SIZE, HttpRoutes.DEFAULT_PAGE_SIZE)
      parameter(HttpRoutes.PAGE, HttpRoutes.DEFAULT_PAGE)
    }.body()
  }

}