package com.chocomiruku.myhome.domain.repository

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addNewUser(user: User)
    fun getCurrentUser(): Flow<Resource<User>>
    fun getUser(userId: String): Flow<Resource<User>>
    suspend fun updateCurrentUser(name: String, email: String, imageUri: String?, notifications: Boolean)
}