package com.dino.core.data.source.remote.service

import com.dino.core.data.source.remote.dto.response.NewsResponse

interface EverythingService {

  suspend fun getEverything(keyword: String): NewsResponse

}