package com.chocomiruku.myhome.domain.usecase.news

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepo: NewsRepository
) {
    operator fun invoke(): Flow<Resource<List<News>>> {
        return newsRepo.getNews()
    }
}