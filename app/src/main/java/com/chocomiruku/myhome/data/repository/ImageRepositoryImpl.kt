package com.chocomiruku.myhome.data.repository

import android.net.Uri
import android.util.Log
import com.chocomiruku.myhome.domain.repository.ImageRepository
import com.chocomiruku.myhome.util.ImageType
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageRepository {

    override suspend fun uploadImage(imageBytes: ByteArray, type: ImageType): Flow<String?> = flow {
        val path = when (type) {
            ImageType.USER -> "profileImages/${UUID.nameUUIDFromBytes(imageBytes)}"
            ImageType.MESSAGE -> "chatImages/${UUID.nameUUIDFromBytes(imageBytes)}"
            ImageType.NEWS -> "newsImages/${UUID.nameUUIDFromBytes(imageBytes)}"
        }

        try {
            val ref = storage.reference.child(path)
            ref.putBytes(imageBytes).await()
            Log.d(TAG, "savingPicture:success")

            val imageUri: String = getImageUri(ref.path)
            emit(imageUri)
        } catch (e: Exception) {
            Log.d(TAG, "savingPicture:failure", e)
            emit(null)
        }
    }

    private suspend fun getImageUri(path: String): String =
        storage.getReference(path).downloadUrl.await().toString()


    private companion object {
        const val TAG = "ImageRepo"
    }
}