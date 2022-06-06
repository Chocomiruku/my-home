package com.chocomiruku.myhome.domain

import com.chocomiruku.myhome.domain.models.User
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addNewUser(user: User)
    fun getCurrentUser() : Flow<User?>
    suspend fun updateCurrentUser(user: User)
    suspend fun uploadProfilePic(picBytes: ByteArray) : Flow<String?>
}