package com.chocomiruku.myhome.domain.usecase.news

import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.domain.repository.NewsRepository
import javax.inject.Inject

class AddNewsUseCase @Inject constructor(
    private val newsRepo: NewsRepository
) {
    suspend operator fun invoke(news: News) {
        return newsRepo.addNews(news)
    }
}