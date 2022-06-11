package com.chocomiruku.myhome.domain.repository

import android.net.Uri
import com.chocomiruku.myhome.util.ImageType
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun uploadImage(imageBytes: ByteArray, type: ImageType): Flow<String?>
}