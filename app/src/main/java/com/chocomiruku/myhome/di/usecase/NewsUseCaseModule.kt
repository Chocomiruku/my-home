package com.chocomiruku.myhome.di.usecase

import com.chocomiruku.myhome.domain.repository.NewsRepository
import com.chocomiruku.myhome.domain.usecase.news.AddNewsUseCase
import com.chocomiruku.myhome.domain.usecase.news.DeleteNewsUseCase
import com.chocomiruku.myhome.domain.usecase.news.GetNewsUseCase
import com.chocomiruku.myhome.domain.usecase.news.UpdateNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class NewsUseCaseModule {
    @Provides
    fun provideAddNewsUseCase(
        newsRepo: NewsRepository
    ): AddNewsUseCase {
        return AddNewsUseCase(newsRepo)
    }

    @Provides
    fun provideDeleteNewsUseCase(
        newsRepo: NewsRepository
    ): DeleteNewsUseCase {
        return DeleteNewsUseCase(newsRepo)
    }

    @Provides
    fun provideGetNewsUseCase(
        newsRepo: NewsRepository
    ): GetNewsUseCase {
        return GetNewsUseCase(newsRepo)
    }

    @Provides
    fun provideUpdateNewsUseCase(
        newsRepo: NewsRepository
    ): UpdateNewsUseCase {
        return UpdateNewsUseCase(newsRepo)
    }
}