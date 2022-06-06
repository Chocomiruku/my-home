package com.chocomiruku.myhome.data.repository

import android.util.Log
import com.chocomiruku.myhome.domain.UserRepository
import com.chocomiruku.myhome.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : UserRepository {

    private val contractsDocRef: DocumentReference
        get() = firestore.document(
            "users/${
                auth.currentUser?.uid
                    ?: throw NullPointerException("UID is null.")
            }"
        )

    private val currentUserDocRef: DocumentReference
        get() = firestore.document(
            "users/${
                auth.currentUser?.uid
                    ?: throw NullPointerException("UID is null.")
            }"
        )

    private val currentUserStorageRef: StorageReference
        get() = storage.reference
            .child(
                auth.currentUser?.uid
                    ?: throw NullPointerException("UID is null.")
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

    override fun getCurrentUser(): Flow<User?> = flow {
        try {
            val user = currentUserDocRef.get().await().toObject(User::class.java)
            emit(user)
        } catch (e: Exception) {
            Log.d(TAG, "gettingUser:failure", e)
            emit(null)
        }
    }

    override suspend fun updateCurrentUser(user: User) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (user.name.isNotBlank()) userFieldMap["name"] = user.name
        if (user.email.isNotBlank()) userFieldMap["email"] = user.email
        if (user.contractNumber.isNotBlank()) userFieldMap["contractNumber"] = user.email
        if (user.profilePicPath != null)
            userFieldMap["profilePicPath"] = user.profilePicPath
        currentUserDocRef.update(userFieldMap).await()
    }

    override suspend fun uploadProfilePic(picBytes: ByteArray): Flow<String?> = flow {
        try {
            val ref = currentUserStorageRef.child("profilePics/${UUID.nameUUIDFromBytes(picBytes)}")
            ref.putBytes(picBytes).await()
            Log.d(TAG, "savingPicture:success")
            emit(ref.path)
        } catch (e: Exception) {
            Log.d(TAG, "savingPicture:failure", e)
            emit(null)
        }
    }

    fun pathToReference(path: String) = storage.getReference(path)

    private companion object {
        const val TAG = "UserRepo"
    }
}