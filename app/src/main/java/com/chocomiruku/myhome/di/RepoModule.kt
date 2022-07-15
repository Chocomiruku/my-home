package com.chocomiruku.myhome.di

import com.chocomiruku.myhome.data.repository.ImageRepositoryImpl
import com.chocomiruku.myhome.data.repository.MessageRepositoryImpl
import com.chocomiruku.myhome.data.repository.NewsRepositoryImpl
import com.chocomiruku.myhome.data.repository.PollRepositoryImpl
import com.chocomiruku.myhome.data.repository.UserRepositoryImpl
import com.chocomiruku.myhome.domain.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Singleton
    @Provides
    fun provideUserRepo(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): UserRepository {
        return UserRepositoryImpl(firestore, auth)
    }

    @Singleton
    @Provides
    fun provideNewsRepo(
        firestore: FirebaseFirestore
    ): NewsRepository {
        return NewsRepositoryImpl(firestore)
    }

    @Singleton
    @Provides
    fun provideMessageRepo(
        firestore: FirebaseFirestore,
        userRepo: UserRepository
    ): MessageRepository {
        return MessageRepositoryImpl(firestore, userRepo)
    }

    @Singleton
    @Provides
    fun providePollRepo(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): PollRepository {
        return PollRepositoryImpl(firestore, auth)
    }

    @Singleton
    @Provides
    fun provideImageRepo(
        storage: FirebaseStorage,
    ): ImageRepository {
        return ImageRepositoryImpl(storage)
    }
}