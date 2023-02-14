package com.dino.core.di

import android.util.Log
import com.dino.core.utils.NdkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun provideKtorHttpClient(): HttpClient {
    return HttpClient(Android) {
      expectSuccess = false
      install(Logging) {
        logger = object : Logger {
          override fun log(message: String) {
            Log.v("==> HttpClientLogger", message)
          }

        }
        level = LogLevel.ALL
      }
      install(HttpTimeout) {
        socketTimeoutMillis = 30000
        requestTimeoutMillis = 30000
        connectTimeoutMillis = 30000
      }
      install(ContentNegotiation) {
        json(Json {
          prettyPrint = true
          isLenient = true
          ignoreUnknownKeys = true
        })
      }
      install(ResponseObserver) {
        onResponse { response ->
          Log.d("Http status: ", "${response.status.value}")
          Log.d("Http response: ", response.toString())
        }
      }
      defaultRequest {
        headers {
          append(HttpHeaders.ContentType, "application/json")
          append(HttpHeaders.Authorization, "Bearer ${NdkUtils.getApiKey()}")
        }
        url(NdkUtils.getBaseUrl())
      }
    }
  }

}