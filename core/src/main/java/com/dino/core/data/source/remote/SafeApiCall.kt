package com.dino.core.data.source.remote

import com.dino.core.data.source.remote.dto.response.ErrorResponse
import com.dino.core.domain.model.Resource
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SafeApiCall {
  suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
  ): Resource<T> {
    return withContext(Dispatchers.IO) {
      try {
        Resource.Success(apiCall.invoke())
      } catch (throwable: Throwable) {
        when (throwable) {
          is IOException -> {
            Resource.Error(-1, "Ups, something error with your internet connection")
          }
          is ClientRequestException -> {
            Resource.Error(throwable.response.status.value, (throwable.response.body() as ErrorResponse).message)
          }
          else -> {
            Resource.Error(-1, "")
          }
        }
      }
    }
  }
}