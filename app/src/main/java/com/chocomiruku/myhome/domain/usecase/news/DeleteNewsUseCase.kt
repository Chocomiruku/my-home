package com.chocomiruku.myhome.domain.usecase.news

import com.chocomiruku.myhome.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteNewsUseCase @Inject constructor(
    private val newsRepo: NewsRepository
) {
    suspend operator fun invoke(newsId: String) {
        return newsRepo.deleteNews(newsId)
    }
}