package com.chocomiruku.myhome.data.repository

import android.util.Log
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    private val currentUserDocRef: DocumentReference
        get() = firestore.document(
            "users/${
                auth.currentUser?.uid
                    ?: throw NullPointerException("UID is null.")
            }"
        )

    override suspend fun addNewUser(user: User) {
        val documentSnapshot = currentUserDocRef.get().await()
        if (!documentSnapshot.exists()) {
            try {
                currentUserDocRef.set(user).await()
            } catch (e: Exception) {
                Log.d(TAG, "addingUser:failure", e)
            }
        }
    }

    override fun getCurrentUser(): Flow<Resource<User>> = flow {
        try {
            val user = currentUserDocRef.get().await().toObject(User::class.java)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "gettingUser:failure", e)
            emit(Resource.Failure(e.message))
        }
    }

    override fun getUser(userId: String): Flow<Resource<User>> = flow {
        try {
            val user = firestore.document(
                "users/${userId}"
            ).get().await().toObject(User::class.java)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "gettingUser:failure", e)
            emit(Resource.Failure(e.message))
        }
    }

    override suspend fun updateCurrentUser(name: String, email: String, imageUri: String?, notifications: Boolean) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (imageUri != null)
            userFieldMap["imageUri"] = imageUri
        userFieldMap["notifications"] = notifications
        try {
            currentUserDocRef.update(userFieldMap).await()
            Log.d(TAG, "updatingUser:success")
        } catch (e: Exception) {
            Log.d(TAG, "updatingUser:failure", e)
        }
    }

    private companion object {
        const val TAG = "UserRepo"
    }
}