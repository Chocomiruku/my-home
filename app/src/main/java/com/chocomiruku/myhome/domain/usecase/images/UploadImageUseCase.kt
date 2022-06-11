package com.chocomiruku.myhome.domain.usecase.images

import com.chocomiruku.myhome.domain.repository.ImageRepository
import com.chocomiruku.myhome.util.ImageType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val imageRepo: ImageRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray, type: ImageType): Flow<String?> {
        return imageRepo.uploadImage(imageBytes, type)
    }
}