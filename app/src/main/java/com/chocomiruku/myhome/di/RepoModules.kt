package com.chocomiruku.myhome.di

import com.chocomiruku.myhome.data.repository.UserRepositoryImpl
import com.chocomiruku.myhome.domain.UserRepository
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
class UserRepoModule {

    @Singleton
    @Provides
    fun provideUserRepo(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        auth: FirebaseAuth
    ): UserRepository {
        return UserRepositoryImpl(firestore, storage, auth)
    }
}