package com.chocomiruku.myhome.data.repository

import android.util.Log
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.domain.repository.NewsRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NewsRepository {

    private val newsCollectionRef: CollectionReference
        get() = firestore.collection("news")

    override suspend fun addNews(news: News) {
        try {
            newsCollectionRef.add(news)
        } catch (e: Exception) {
            Log.d(TAG, "addingNews:failure", e)
        }
    }

    override suspend fun deleteNews(newsId: String) {
        try {
            val newsDocRef = firestore.document("news/${newsId}")
            newsDocRef.delete()
        } catch (e: Exception) {
            Log.d(TAG, "deletingNews:failure", e)
        }
    }

    override suspend fun updateNews(newsId: String, title: String, text: String, imagePath: String?) {
        val newsFieldMap = mutableMapOf<String, Any>()
        if (title.isNotBlank()) newsFieldMap["title"] = title
        if (text.isNotBlank()) newsFieldMap["text"] = text
        if (imagePath != null)
            newsFieldMap["imageUri"] = imagePath
        try {
            val newsDocRef = firestore.document("news/${newsId}")
            newsDocRef.update(newsFieldMap).await()
            Log.d(TAG, "updatingNews:success")
        } catch (e: Exception) {
            Log.d(TAG, "updatingNews:failure", e)
        }
    }

    override fun getNews(): Flow<Resource<List<News>>> = callbackFlow {
        val listener = newsCollectionRef.addSnapshotListener { querySnapshot, firebaseException ->
            if (firebaseException != null) {
                Log.e(TAG, "addingListener:failure", firebaseException)
                return@addSnapshotListener
            }

            val newsList = mutableListOf<News>()
            querySnapshot?.documents?.forEach { documentSnapshot ->
                documentSnapshot.toObject(News::class.java)?.let { news ->
                    newsList.add(news)
                }
            }
            trySend(Resource.Success(newsList.sortedByDescending { news -> news.date }.toList()))
        }

        awaitClose { listener.remove() }
    }

    private companion object {
        const val TAG = "NewsRepo"
    }
}