package com.dino.core.di

import com.dino.core.data.repository.EverythingRepositoryImpl
import com.dino.core.data.repository.HeadlinesRepositoryImpl
import com.dino.core.data.source.remote.service.EverythingService
import com.dino.core.data.source.remote.service.HeadlinesService
import com.dino.core.domain.repository.EverythingRepository
import com.dino.core.domain.repository.HeadlinesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

  @Provides
  fun provideEverythingRepository(service: EverythingService): EverythingRepository {
    return EverythingRepositoryImpl(service)
  }

  @Provides
  fun provideHeadlinesRepository(service: HeadlinesService): HeadlinesRepository {
    return HeadlinesRepositoryImpl(service)
  }

}