package com.dino.core.di

import com.dino.core.data.source.remote.service.EverythingService
import com.dino.core.data.source.remote.service.HeadlinesService
import com.dino.core.data.source.remote.service.impl.EverythingServiceImpl
import com.dino.core.data.source.remote.service.impl.HeadlinesServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteSourceModule {

  @Provides
  @Singleton
  fun provideEverythingService(httpClient: HttpClient): EverythingService {
    return EverythingServiceImpl(httpClient)
  }

  @Provides
  @Singleton
  fun provideHeadlinesService(httpClient: HttpClient): HeadlinesService {
    return HeadlinesServiceImpl(httpClient)
  }

}