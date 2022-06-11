package com.chocomiruku.myhome.di.usecase

import com.chocomiruku.myhome.domain.repository.ImageRepository
import com.chocomiruku.myhome.domain.usecase.images.UploadImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ImageUseCaseModule {
    @Provides
    fun provideUploadImageUseCase(
        imageRepo: ImageRepository
    ): UploadImageUseCase {
        return UploadImageUseCase(imageRepo)
    }
}