package com.chocomiruku.myhome.domain.repository

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun addNews(news: News)
    suspend fun deleteNews(newsId: String)
    suspend fun updateNews(newsId: String, title: String, text: String, imagePath: String?)
    fun getNews(): Flow<Resource<List<News>>>
}