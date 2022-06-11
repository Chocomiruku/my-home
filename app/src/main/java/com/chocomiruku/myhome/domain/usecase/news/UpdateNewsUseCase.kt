package com.chocomiruku.myhome.domain.usecase.news

import com.chocomiruku.myhome.domain.repository.NewsRepository
import javax.inject.Inject

class UpdateNewsUseCase @Inject constructor(
    private val newsRepo: NewsRepository
) {
    suspend operator fun invoke(newsId: String, title: String, text: String, imagePath: String?) {
        return newsRepo.updateNews(newsId, title, text, imagePath)
    }
}