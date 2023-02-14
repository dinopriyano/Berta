package com.dino.core.di

import com.dino.core.data.interactors.EverythingUseCaseImpl
import com.dino.core.data.interactors.HeadlinesUseCaseImpl
import com.dino.core.domain.repository.EverythingRepository
import com.dino.core.domain.repository.HeadlinesRepository
import com.dino.core.domain.use_case.EverythingUseCase
import com.dino.core.domain.use_case.HeadlinesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorModule {

  @Provides
  fun provideEverythingInteractor(repository: EverythingRepository): EverythingUseCase {
    return EverythingUseCaseImpl(repository)
  }

  @Provides
  fun provideHeadlinesInteractor(repository: HeadlinesRepository): HeadlinesUseCase {
    return HeadlinesUseCaseImpl(repository)
  }

}