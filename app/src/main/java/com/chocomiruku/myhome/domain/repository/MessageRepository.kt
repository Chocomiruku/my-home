package com.chocomiruku.myhome.domain.repository

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(message: Message)
    fun getMessages(): Flow<Resource<List<Message>>>
}